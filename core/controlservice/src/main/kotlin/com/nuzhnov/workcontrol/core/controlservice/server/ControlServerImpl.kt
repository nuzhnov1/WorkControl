package com.nuzhnov.workcontrol.core.controlservice.server

import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerException.*
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerState.*
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerError.*
import com.nuzhnov.workcontrol.core.controlservice.model.ClientApiModel
import com.nuzhnov.workcontrol.core.controlservice.model.ControlServerResponse
import com.nuzhnov.workcontrol.core.controlservice.common.*
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
import java.time.Duration
import java.time.LocalTime
import kotlin.properties.Delegates

internal class ControlServerImpl : IControlServer {

    private val _clients = MutableStateFlow(value = mutableSetOf<ClientApiModel>())
    override val clients: StateFlow<Set<ClientApiModel>> get() = _clients.asStateFlow()

    private val _serverState = MutableStateFlow<ControlServerState>(value = NotRunning)
    override val serverState = _serverState.asStateFlow()

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

    private val isStoppedAcceptConnections get() = _serverState.value is StoppedAcceptConnections
    private val isNoClients get() = clientConnections.isEmpty()


    override suspend fun start(
        address: InetAddress,
        port: Int,
        backlog: Int,
        maxAcceptConnectionAttempts: Int
    ) {
        initProperties(address, port, backlog, maxAcceptConnectionAttempts)

        try {
            initControlServer()
                .onSuccess { runControlServer() }
                .onFailure { cause -> onInitFailed(cause) }
        } catch (exception: CancellationException) {
            _serverState.value = _serverState.value.toNextStateWhenCancelled()
        } catch (exception: ControlServerException) {
            _serverState.value = exception.toControlServerState()
        } catch (exception: IOException) {
            _serverState.value = Stopped(address, port, error = IO_ERROR, cause = exception)
        } catch (exception: SecurityException) {
            _serverState.value = Stopped(address, port, error = SECURITY_ERROR, cause = exception)
        } catch (exception: Throwable) {
            _serverState.value = Stopped(address, port, error = UNKNOWN_ERROR, cause =  exception)
        } finally {
            finishControlServer()
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

    private fun initControlServer() = runCatching {
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

            _serverState.value = Running(address, port)
        }
    }

    private fun onInitFailed(cause: Throwable): Nothing = when (cause) {
        is SecurityException -> throw cause
        else -> throw InitException(cause)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun runControlServer(): Nothing {
        val selector = selector!!

        acceptConnectionAttempts = 0

        while (true) {
            if (isStoppedAcceptConnections && isNoClients) {
                throw CancellationException()
            }

            if (selector.isEventsNotOccurred) {
                yield()
                continue
            }

            val selectedKeys = selector.selectedKeys
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = iterator.next().also { iterator.remove() }

                if (key.isAcceptable) {
                    key.server?.apply {
                        onAcceptClient(selector)
                            .onSuccess { acceptConnectionAttempts = 0 }
                            .onFailure { cause -> onFailedAcceptClient(cause) }
                    }
                }

                if (key.isReadable) {
                    key.client?.apply { onClientRead().onFailure { disconnectClient() } }
                }
            }
        }
    }

    private fun finishControlServer() = runCatching {
        clientConnections.disconnectAll()
        runCatching { selector?.close() }
        runCatching { serverSocketChannel?.close() }

        Unit
    }

    private fun ServerSocketChannel.onAcceptClient(selector: Selector) = runCatching {
        accept().apply { connectClient(selector) }
    }

    private fun ServerSocketChannel.onFailedAcceptClient(cause: Throwable) {
        acceptConnectionAttempts++

        if (acceptConnectionAttempts > maxAcceptConnectionAttempts) {
            onStoppedAcceptConnections(cause)
        }
    }

    private fun ServerSocketChannel.onStoppedAcceptConnections(cause: Throwable) = runCatching {
        close()
        _serverState.value = StoppedAcceptConnections(
            address = address,
            port = port,
            error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
            cause = MaxAcceptConnectionAttemptsReachedException(cause)
        )
    }

    private fun SocketChannel.onClientRead() = runCatching {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress
        val clientID = clientConnections[clientAddress]?.id

        val readBytesCount = readClientID()

        when {
            readBytesCount < 0 -> disconnectClient()

            readBytesCount != inputBufferSize -> {
                updateClientActivity(clientID, isActiveNow = false)
                writeResponse(ControlServerResponse.BAD_REQUEST)
            }

            else -> {
                val id = inputBuffer.long

                if (clientID == null) {
                    clientConnections[clientAddress] = ClientConnection(channel = this, id = id)
                    updateClientActivity(id, isActiveNow = true)
                } else {
                    updateClientActivity(clientID, isActiveNow = true)
                }

                writeResponse(ControlServerResponse.OK)
            }
        }

        Unit
    }

    private fun SocketChannel.connectClient(selector: Selector) {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress

        configureBlocking(/* block = */ false)
        register(selector, /* ops = */ OP_READ or OP_WRITE)

        clientConnections[clientAddress] = ClientConnection(channel = this, id = null)
    }

    private fun SocketChannel.disconnectClient() = runCatching {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress

        clientConnections.remove(clientAddress)?.also { clientConnection ->
            updateClientActivity(clientID = clientConnection.id, isActiveNow = false)
            clientConnection.channel.apply { close() }
        }

        Unit
    }

    private fun MutableMap<SocketAddress, ClientConnection>.disconnectAll() {
        values.map { it.channel }.forEach { channel ->
            channel.apply {
                safeWriteResponse(ControlServerResponse.SHUTDOWN_SERVER)
                disconnectClient()
            }
        }
    }

    private fun SocketChannel.readClientID(): Int {
        inputBuffer.clear()
        return read(inputBuffer)
    }

    private fun SocketChannel.writeResponse(serverResponse: ControlServerResponse): Int {
        outputBuffer.apply {
            clear()
            putInt(serverResponse.ordinal)
            flip()
        }

        return write(outputBuffer)
    }

    private fun SocketChannel.safeWriteResponse(serverResponse: ControlServerResponse) =
        runCatching { writeResponse(serverResponse) }

    private fun updateClientActivity(clientID: Long?, isActiveNow: Boolean) {
        if (clientID != null) {
            _clients.update { clients ->
                clients.apply { updateClientActivity(clientID, isActiveNow) }
            }
        }
    }

    private fun removeAllClients() {
        _clients.update { clients -> clients.apply { clear() } }
    }

    private fun MutableSet<ClientApiModel>.updateClientActivity(
        clientID: Long,
        isActiveNow: Boolean
    ) {
        val client = this[clientID]
        val now = LocalTime.now()

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
            val delta = Duration.between(previouslyVisit, now)

            this[clientID] = ClientApiModel(
                id = clientID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { previouslyVisit },
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }

    private operator fun MutableSet<ClientApiModel>.get(clientID: Long) =
        find { model -> model.id == clientID }

    private operator fun MutableSet<ClientApiModel>.set(
        clientID: Long,
        addedModel: ClientApiModel
    ) = removeIf { model -> model.id == clientID }.also { add(addedModel) }


    private data class ClientConnection(
        val channel: SocketChannel,
        val id: Long?
    )
}
