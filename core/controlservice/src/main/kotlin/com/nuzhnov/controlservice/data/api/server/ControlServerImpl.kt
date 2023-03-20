package com.nuzhnov.controlservice.data.api.server

import com.nuzhnov.controlservice.data.api.server.ControlServerException.*
import com.nuzhnov.controlservice.data.api.server.ControlServerState.*
import com.nuzhnov.controlservice.data.api.server.ControlServerError.*
import com.nuzhnov.controlservice.data.api.model.ClientApiModel
import com.nuzhnov.controlservice.data.api.model.ControlServerResponse
import com.nuzhnov.controlservice.data.api.util.*
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

internal class ControlServerImpl : IControlServer {

    private val _clients = MutableStateFlow(value = mutableMapOf<Long, ClientApiModel>())
    override val clients: Flow<List<ClientApiModel>> get() = _clients.map { it.values.toList() }

    private val _serverState = MutableStateFlow<ControlServerState>(value = NotRunning)
    override val serverState = _serverState.asStateFlow()

    private val clientConnections = mutableMapOf<SocketAddress, ClientConnection>()
    private var selector: Selector? = null
    private var serverSocketChannel: ServerSocketChannel? = null

    private var acceptConnectionAttempts = 0
    private var maxAcceptConnectionAttempts = 0

    private val inputBufferSize = Long.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Int.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)

    private val isStoppedAcceptConnections get() = _serverState.value is StoppedAcceptConnections
    private val isNoClients get() = clientConnections.isEmpty()


    override suspend fun start(
        serverAddress: InetAddress,
        backlog: Int,
        maxAcceptConnectionAttempts: Int
    ) {
        checkArguments(maxAcceptConnectionAttempts)

        this.maxAcceptConnectionAttempts = maxAcceptConnectionAttempts

        try {
            initControlServer(serverAddress, backlog)
                .onSuccess { runControlServer() }
                .onFailure { cause -> onInitFailed(cause) }
        } catch (exception: CancellationException) {
            _serverState.value = _serverState.value.toNextStateWhenCancelled()
        } catch (exception: ControlServerException) {
            _serverState.value = exception.toControlServerState()
        } catch (exception: IOException) {
            _serverState.value = Stopped(error = IO_ERROR, cause = exception)
        } catch (exception: SecurityException) {
            _serverState.value = Stopped(error = SECURITY_ERROR, cause = exception)
        } catch (exception: Throwable) {
            _serverState.value = Stopped(error = UNKNOWN_ERROR, cause =  exception)
        } finally {
            finishControlServer()
        }
    }

    private fun checkArguments(maxAcceptConnectionAttempts: Int) {
        if (maxAcceptConnectionAttempts < 0) {
            throw IllegalArgumentException("maxAcceptConnectionAttempts < 0")
        }
    }

    private fun ControlServerState.toNextStateWhenCancelled() = when (this) {
        is Running -> NotRunning
        is StoppedAcceptConnections -> Stopped(error, cause)
        else -> this
    }

    private fun ControlServerException.toControlServerState() = when (this) {
        is InitException -> Stopped(
            error = INIT_ERROR,
            cause = cause
        )

        is MaxAcceptConnectionAttemptsReachedException -> Stopped(
            error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED_ERROR,
            cause = cause
        )
    }

    private fun initControlServer(serverAddress: InetAddress, backlog: Int) = runCatching {
        selector = Selector.open()
        serverSocketChannel = ServerSocketChannel.open().apply {
            val selector = selector!!
            val serverSocket = socket()
            val serverSocketAddress = InetSocketAddress(serverAddress, /* port = */ 0)

            configureBlocking(/* block = */ false)
            serverSocket.bind(serverSocketAddress, backlog)
            register(selector, OP_ACCEPT)

            _serverState.value = Running(
                serverAddress = serverSocket.inetAddress,
                serverPort = serverSocket.localPort
            )
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

                when {
                    key.isAcceptable -> key.server?.apply {
                        onAcceptClient(selector)
                            .onSuccess { acceptConnectionAttempts = 0 }
                            .onFailure { cause -> onFailedAcceptClient(cause) }
                    }

                    key.isReadable -> key.client?.apply {
                        onReadClient().onFailure { disconnectClient() }
                    }

                    key.isWritable -> Unit
                }
            }
        }
    }

    private fun finishControlServer() = runCatching {
        removeAllClients()

        clientConnections.disconnectAll()
        runCatching { selector?.close() }
        runCatching { serverSocketChannel?.close() }
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
            error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED_ERROR,
            cause = MaxAcceptConnectionAttemptsReachedException(cause)
        )
    }

    private fun SocketChannel.onReadClient() = runCatching {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress
        val clientID = clientConnections[clientAddress]?.id

        val readBytesCount = readData()

        when {
            readBytesCount < 0 -> {
                updateClient(clientID, isActiveNow = false)
                disconnectClient()
            }

            readBytesCount != inputBufferSize -> {
                updateClient(clientID, isActiveNow = false)
                writeData(ControlServerResponse.BAD_REQUEST)
            }

            else -> {
                val id = inputBuffer.long

                if (clientID == null) {
                    clientConnections[clientAddress] = ClientConnection(channel = this, id = id)
                    updateClient(id, isActiveNow = true)
                } else {
                    updateClient(clientID, isActiveNow = true)
                }

                writeData(ControlServerResponse.OK)
            }
        }

        readBytesCount
    }

    private fun SocketChannel.connectClient(selector: Selector) {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress

        configureBlocking(/* block = */ false)
        register(selector, /* ops = */ OP_READ or OP_WRITE)

        clientConnections[clientAddress] = ClientConnection(
            channel = this,
            id = null
        )
    }

    private fun SocketChannel.disconnectClient() = runCatching {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress

        clientConnections.remove(clientAddress)?.also { clientConnection ->
            clientConnection.channel.apply { close() }
        }

        Unit
    }

    private fun MutableMap<SocketAddress, ClientConnection>.disconnectAll() {
        values.map { it.channel }.forEach { channel ->
            channel.apply {
                safeWriteData(ControlServerResponse.SHUTDOWN_SERVER)
                disconnectClient()
            }
        }
    }

    private fun SocketChannel.readData(): Int {
        inputBuffer.clear()
        return read(inputBuffer)
    }

    private fun SocketChannel.writeData(serverResponse: ControlServerResponse): Int {
        outputBuffer.apply {
            clear()
            putInt(serverResponse.ordinal)
            flip()
        }

        return write(outputBuffer)
    }

    private fun SocketChannel.safeWriteData(serverResponse: ControlServerResponse) = runCatching {
        writeData(serverResponse)
    }

    private fun updateClient(clientID: Long?, isActiveNow: Boolean) {
        if (clientID != null) {
            _clients.update { clients -> clients.apply { updateClient(clientID, isActiveNow) } }
        }
    }

    private fun removeAllClients() {
        _clients.update { clients -> clients.apply { clear() } }
    }

    // TODO: replace time methods with those that support api below 26
    private fun MutableMap<Long, ClientApiModel>.updateClient(
        uniqueID: Long,
        isActiveNow: Boolean
    ) {
        val client = this[uniqueID]
        val now = LocalTime.now()

        if (client?.lastVisit == null) {
            this[uniqueID] = ClientApiModel(
                uniqueID = uniqueID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { null },
                totalVisitDuration = Duration.ZERO
            )
        } else {
            val isPreviouslyActive = client.isActive
            val previouslyVisit = client.lastVisit
            val duration = client.totalVisitDuration
            val delta = Duration.between(previouslyVisit, now)

            this[uniqueID] = ClientApiModel(
                uniqueID = uniqueID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { previouslyVisit },
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }


    private data class ClientConnection(
        val channel: SocketChannel,
        val id: Long?
    )
}
