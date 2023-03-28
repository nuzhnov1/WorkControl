package com.nuzhnov.workcontrol.core.visitcontrol.server

import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerException.*
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerState.*
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerError.*
import com.nuzhnov.workcontrol.core.visitcontrol.model.Visitor
import com.nuzhnov.workcontrol.core.visitcontrol.model.ServerResponse
import com.nuzhnov.workcontrol.core.visitcontrol.common.*
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

internal class ServerImpl : Server {

    private val _visitorsMap = mutableMapOf<Long, Visitor>()
    private val _visitors = MutableStateFlow(value = _visitorsMap.values.toSet())
    override val visitors: StateFlow<Set<Visitor>> = _visitors.asStateFlow()

    private val _state = MutableStateFlow<ServerState>(value = NotRunning)
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
        } catch (exception: ServerException) {
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

    private fun ServerState.toNextStateWhenCancelled() = when (this) {
        is Running -> NotRunning
        is StoppedAcceptConnections -> Stopped(address, port, error, cause)
        else -> this
    }

    private fun ServerException.toControlServerState() = when (this) {
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
        removeAllVisitors()

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
                        receiveVisitorID()
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

            clientConnections[clientAddress] = ClientConnection(channel = this, visitorID = null)
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

    private fun SocketChannel.receiveVisitorID() = applyCatching {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress
        val visitorID = clientConnections[clientAddress]?.visitorID

        val readBytesCount = readVisitorID()

        when {
            readBytesCount < 0 -> disconnectClient()

            readBytesCount == inputBufferSize -> {
                val id = inputBuffer.long

                if (visitorID == null) {
                    clientConnections[clientAddress] = ClientConnection(
                        channel = this,
                        visitorID = id
                    )
                    updateVisitorActivity(id, isActiveNow = true)
                } else {
                    updateVisitorActivity(visitorID, isActiveNow = true)
                }

                writeResponse(ServerResponse.OK)
            }

            else -> {
                if (visitorID != null) {
                    updateVisitorActivity(visitorID, isActiveNow = false)
                }

                writeResponse(ServerResponse.BAD_REQUEST)
            }
        }
    }

    private fun SocketChannel.disconnectClient(serverResponse: ServerResponse? = null) {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress

        clientConnections.remove(clientAddress)?.also { (_, visitorID) ->
            if (visitorID != null) {
                updateVisitorActivity(visitorID = visitorID, isActiveNow = false)
            }
        }

        if (serverResponse != null) {
            safeWriteResponse(serverResponse)
        }

        safeClose()
    }

    private fun MutableMap<SocketAddress, ClientConnection>.disconnectAll() {
        values.forEach { (channel, visitorID) ->
            if (visitorID != null) {
                updateVisitorActivity(visitorID = visitorID, isActiveNow = false)
            }

            channel.safeWriteResponse(ServerResponse.SHUTDOWN_SERVER)
            channel.safeClose()
        }

        clear()
    }

    private fun SocketChannel.readVisitorID(): Int {
        inputBuffer.clear()
        return read(inputBuffer).also { inputBuffer.flip() }
    }

    private fun SocketChannel.writeResponse(serverResponse: ServerResponse): Int {
        outputBuffer.apply {
            clear()
            putInt(serverResponse.ordinal)
            flip()
        }

        return write(outputBuffer)
    }

    private fun SocketChannel.safeWriteResponse(
        serverResponse: ServerResponse
    ) = runCatching { writeResponse(serverResponse) }

    private fun updateVisitorActivity(visitorID: Long, isActiveNow: Boolean) {
        _visitorsMap.updateVisitorActivity(visitorID, isActiveNow)
        _visitors.value = _visitorsMap.values.toSet()
    }

    private fun removeAllVisitors() {
        _visitorsMap.clear()
        _visitors.value = setOf()
    }

    private fun MutableMap<Long, Visitor>.updateVisitorActivity(
        visitorID: Long,
        isActiveNow: Boolean
    ) {
        val visitor = this[visitorID]
        val now = DateTime.now()

        if (visitor?.lastVisit == null) {
            this[visitorID] = Visitor(
                id = visitorID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { null },
                totalVisitDuration = Duration.ZERO
            )
        } else {
            val isPreviouslyActive = visitor.isActive
            val previouslyVisit = visitor.lastVisit
            val duration = visitor.totalVisitDuration
            val delta = Duration(previouslyVisit, now)

            this[visitorID] = Visitor(
                id = visitorID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { previouslyVisit },
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }


    private data class ClientConnection(val channel: SocketChannel, val visitorID: Long?)
}
