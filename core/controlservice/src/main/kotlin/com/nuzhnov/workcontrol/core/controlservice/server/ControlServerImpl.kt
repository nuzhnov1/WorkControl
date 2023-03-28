package com.nuzhnov.workcontrol.core.controlservice.server

import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerException.*
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerState.*
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerError.*
import com.nuzhnov.workcontrol.core.controlservice.model.ClientApiModel
import com.nuzhnov.workcontrol.core.controlservice.model.ControlServerResponse
import com.nuzhnov.workcontrol.core.controlservice.common.*
import kotlin.properties.Delegates
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.yield
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey.*
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import org.joda.time.DateTime
import org.joda.time.Duration

internal class ControlServerImpl : IControlServer {

    private val _clientsMap = mutableMapOf<Long, ClientApiModel>()
    private val _clients = MutableStateFlow(value = _clientsMap.values.toSet())
    override val clients: StateFlow<Set<ClientApiModel>> = _clients.asStateFlow()

    private val _state = MutableStateFlow<ControlServerState>(value = NotRunning)
    override val state = _state.asStateFlow()

    private lateinit var address: InetAddress
    private var port by Delegates.notNull<Int>()
    private var backlog by Delegates.notNull<Int>()
    private var maxAcceptConnectionAttempts by Delegates.notNull<Int>()

    private val clientConnections = mutableMapOf<SocketAddress, ClientConnection>()
    private var selector: Selector? = null
    private var serverSocketChannel: ServerSocketChannel? = null
    private var acceptConnectionAttempts = 0

    private val inputBufferSize = Long.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Int.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)

    private val isStoppedAcceptConnections get() = _state.value is StoppedAcceptConnections
    private val isNoClients get() = clientConnections.isEmpty()


    override suspend fun start(
        address: InetAddress,
        port: Int,
        backlog: Int,
        maxAcceptConnectionAttempts: Int
    ) {
        initProperties(address, port, backlog, maxAcceptConnectionAttempts)

        try {
            initServer()
                .onSuccess { runServer() }
                .onFailure { cause -> onInitFailed(cause) }
        } catch (exception: CancellationException) {
            _state.value = _state.value.toNextStateWhenCancelled()
        } catch (exception: ControlServerException) {
            _state.value = exception.toControlServerState()
        } catch (exception: IOException) {
            _state.value = Stopped(address, port, error = IO_ERROR, cause = exception)
        } catch (exception: SecurityException) {
            _state.value = Stopped(address, port, error = SECURITY_ERROR, cause = exception)
        } catch (exception: Throwable) {
            _state.value = Stopped(address, port, error = UNKNOWN_ERROR, cause =  exception)
        } finally {
            finishServer()
        }
    }

    private fun initProperties(
        address: InetAddress,
        port: Int,
        backlog: Int,
        maxAcceptConnectionAttempts: Int
    ) {
        if (maxAcceptConnectionAttempts < 0) {
            throw IllegalArgumentException("maxAcceptConnectionAttempts < 0")
        }

        this.address = address
        this.port = port
        this.backlog = backlog
        this.maxAcceptConnectionAttempts = maxAcceptConnectionAttempts
    }

    private fun ControlServerState.toNextStateWhenCancelled() = when (this) {
        is Running -> NotRunning
        is StoppedAcceptConnections -> Stopped(address, port, error, cause)
        else -> this
    }

    private fun ControlServerException.toControlServerState() = when (this) {
        is InitException -> Stopped(
            address = address,
            port = port,
            error = INIT_ERROR,
            cause = cause
        )

        is MaxAcceptConnectionAttemptsReachedException -> Stopped(
            address = address,
            port = port,
            error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
            cause = cause
        )
    }

    private fun initServer() = applyCatching {
        removeAllClients()

        selector = Selector.open()
        serverSocketChannel = ServerSocketChannel.open().apply {
            val selector = selector!!
            val socket = socket()
            val serverSocketAddress = InetSocketAddress(address, port)

            configureBlocking(/* block = */ false)
            socket.bind(serverSocketAddress, backlog)
            register(selector, OP_ACCEPT)

            address = socket.inetAddress
            port = socket.localPort

            _state.value = Running(address, port)
        }
    }

    private fun onInitFailed(cause: Throwable): Nothing = when (cause) {
        is SecurityException -> throw cause
        else -> throw InitException(cause)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun runServer(): Nothing {
        val selector = selector!!

        acceptConnectionAttempts = 0

        while (true) {
            yield()

            if (isStoppedAcceptConnections && isNoClients) {
                throw CancellationException()
            }

            if (selector.isEventsNotOccurred) {
                continue
            }

            val selectedKeys = selector.selectedKeys
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = iterator.next().also { iterator.remove() }

                if (key.isAcceptable) {
                    key.server?.apply {
                        acceptClient(selector)
                            .onSuccess { acceptConnectionAttempts = 0 }
                            .onFailure { cause -> onFailedAcceptClient(cause) }
                    }
                }

                if (key.isReadable) {
                    key.client?.apply {
                        receiveClientID()
                            .onFailure { disconnectClient() }
                    }
                }
            }
        }
    }

    private fun finishServer() = applyCatching {
        clientConnections.disconnectAll()
        selector?.safeClose()
        serverSocketChannel?.safeClose()
    }

    private fun ServerSocketChannel.acceptClient(selector: Selector) = runCatching {
        accept().apply {
            val clientSocket = socket()
            val clientAddress = clientSocket.remoteSocketAddress

            configureBlocking(/* block = */ false)
            register(selector, /* ops = */ OP_READ or OP_WRITE)

            clientConnections[clientAddress] = ClientConnection(channel = this, id = null)
        }
    }

    private fun ServerSocketChannel.onFailedAcceptClient(cause: Throwable) {
        acceptConnectionAttempts++

        if (acceptConnectionAttempts > maxAcceptConnectionAttempts) {
            onStoppedAcceptConnections(cause)
        }
    }

    private fun ServerSocketChannel.onStoppedAcceptConnections(cause: Throwable) {
        safeClose().onSuccess {
            _state.value = StoppedAcceptConnections(
                address = address,
                port = port,
                error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
                cause = MaxAcceptConnectionAttemptsReachedException(cause)
            )
        }
    }

    private fun SocketChannel.receiveClientID() = applyCatching {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress
        val clientID = clientConnections[clientAddress]?.id

        val readBytesCount = readClientID()

        when {
            readBytesCount < 0 -> disconnectClient()

            readBytesCount == inputBufferSize -> {
                val id = inputBuffer.long

                if (clientID == null) {
                    clientConnections[clientAddress] = ClientConnection(channel = this, id = id)
                    updateClientActivity(id, isActiveNow = true)
                } else {
                    updateClientActivity(clientID, isActiveNow = true)
                }

                writeResponse(ControlServerResponse.OK)
            }

            else -> {
                if (clientID != null) {
                    updateClientActivity(clientID, isActiveNow = false)
                }

                writeResponse(ControlServerResponse.BAD_REQUEST)
            }
        }
    }

    private fun SocketChannel.disconnectClient(serverResponse: ControlServerResponse? = null) {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress

        clientConnections.remove(clientAddress)?.also { (_, id) ->
            if (id != null) {
                updateClientActivity(clientID = id, isActiveNow = false)
            }
        }

        if (serverResponse != null) {
            safeWriteResponse(serverResponse)
        }

        safeClose()
    }

    private fun MutableMap<SocketAddress, ClientConnection>.disconnectAll() {
        values.forEach { (channel, id) ->
            if (id != null) {
                updateClientActivity(clientID = id, isActiveNow = false)
            }

            channel.safeWriteResponse(ControlServerResponse.SHUTDOWN_SERVER)
            channel.safeClose()
        }

        clear()
    }

    private fun SocketChannel.readClientID(): Int {
        inputBuffer.clear()
        return read(inputBuffer).also { inputBuffer.flip() }
    }

    private fun SocketChannel.writeResponse(serverResponse: ControlServerResponse): Int {
        outputBuffer.apply {
            clear()
            putInt(serverResponse.ordinal)
            flip()
        }

        return write(outputBuffer)
    }

    private fun SocketChannel.safeWriteResponse(
        serverResponse: ControlServerResponse
    ) = runCatching { writeResponse(serverResponse) }

    private fun updateClientActivity(clientID: Long, isActiveNow: Boolean) {
        _clientsMap.updateClientActivity(clientID, isActiveNow)
        _clients.value = _clientsMap.values.toSet()
    }

    private fun removeAllClients() {
        _clientsMap.clear()
        _clients.value = setOf()
    }

    private fun MutableMap<Long, ClientApiModel>.updateClientActivity(
        clientID: Long,
        isActiveNow: Boolean
    ) {
        val client = this[clientID]
        val now = DateTime.now()

        if (client?.lastVisit == null) {
            this[clientID] = ClientApiModel(
                id = clientID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { null },
                totalVisitDuration = Duration.ZERO
            )
        } else {
            val isPreviouslyActive = client.isActive
            val previouslyVisit = client.lastVisit
            val duration = client.totalVisitDuration
            val delta = Duration(previouslyVisit, now)

            this[clientID] = ClientApiModel(
                id = clientID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { previouslyVisit },
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }


    private data class ClientConnection(val channel: SocketChannel, val id: Long?)
}
