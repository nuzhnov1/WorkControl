package com.nuzhnov.workcontrol.common.visitcontrol.visitor

import com.nuzhnov.workcontrol.common.visitcontrol.model.ServerResponse
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.util.*
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorError.*
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorException.*
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorState.*
import com.nuzhnov.workcontrol.common.util.applyCatching
import com.nuzhnov.workcontrol.common.util.transformFailedCause
import kotlin.properties.Delegates
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey.*
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

internal class VisitorImpl : Visitor {

    private val _state = MutableStateFlow<VisitorState>(value = NotRunningYet)
    override val state = _state.asStateFlow()

    private var clientJob: Job? = null

    private var serverAddress by Delegates.notNull<InetAddress>()
    private var serverPort by Delegates.notNull<Int>()
    private var visitorID by Delegates.notNull<VisitorID>()

    private var clientSelector: Selector? = null
    private var clientSocketChannel: SocketChannel? = null

    private val inputBufferSize = Int.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Long.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)


    override suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) = withContext(context = Dispatchers.IO) {
        initProperties(serverAddress, serverPort, visitorID)
        clientJob?.cancelAndJoin()

        val newClientJob = launchClientJob()

        clientJob = newClientJob
        newClientJob.join()
    }

    override fun stopVisit() {
        clientJob?.cancel()
    }

    private fun initProperties(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) {
        this.serverAddress = serverAddress
        this.serverPort = serverPort
        this.visitorID = visitorID
    }

    private fun CoroutineScope.launchClientJob(): Job = launch {
        val jobResult = initiateConnection().fold(
            onSuccess = {
                startClientJob().fold(
                    onSuccess = { Result.success(Unit) },
                    onFailure = { cause -> Result.failure(cause) }
                )
            },

            onFailure = { cause -> Result.failure(cause) }
        )

        updateState(state = jobResult.toVisitorState())
        completeClientJob()
    }

    private fun Result<Unit>.toVisitorState(): VisitorState = fold(
        onSuccess = { _state.value.toNextStateOnNormalCompletion() },
        onFailure = { cause ->
            when (cause) {
                is CancellationException -> _state.value.toNextStateOnNormalCompletion()
                is VisitorException -> cause.toVisitorState()

                is IOException -> StoppedByError(
                    serverAddress = serverAddress,
                    serverPort = serverPort,
                    visitorID = visitorID,
                    error = IO_ERROR,
                    cause = cause
                )

                is SecurityException -> StoppedByError(
                    serverAddress = serverAddress,
                    serverPort = serverPort,
                    visitorID = visitorID,
                    error = SECURITY_ERROR,
                    cause = cause
                )

                else -> StoppedByError(
                    serverAddress = serverAddress,
                    serverPort = serverPort,
                    visitorID = visitorID,
                    error = UNKNOWN_ERROR,
                    cause = cause
                )
            }
        }
    )

    private fun VisitorState.toNextStateOnNormalCompletion(): VisitorState =
        when (this) {
            is Connecting   -> Stopped(serverAddress, serverPort, visitorID)
            is Running      -> Stopped(serverAddress, serverPort, visitorID)
            else            -> this
        }

    private fun VisitorException.toVisitorState(): VisitorState =
        when (this) {
            is ConnectionFailedException -> StoppedByError(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = visitorID,
                error = CONNECTION_FAILED,
                cause = cause
            )

            is BreakConnectionException -> StoppedByError(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = visitorID,
                error = BREAK_CONNECTION,
                cause = cause
            )

            is BadConnectionException -> StoppedByError(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = visitorID,
                error = BAD_CONNECTION,
                cause = cause
            )

            is DisconnectedException -> StoppedByError(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = visitorID,
                error = DISCONNECTED,
                cause = cause
            )
        }

    private suspend fun initiateConnection(): Result<Unit> = applyCatching {
        clientSelector = Selector.open()
        clientSocketChannel = SocketChannel.open().apply {
            val selector = clientSelector!!
            val socket = socket()
            val serverSocketAddress = InetSocketAddress(serverAddress, serverPort)

            configureBlocking(/* block = */ false)
            connect(serverSocketAddress)
            register(selector, /* ops = */ OP_READ or OP_WRITE)

            serverAddress = socket.inetAddress
            serverPort = socket.port

            updateState(state = Connecting(serverAddress, serverPort, visitorID))
            awaitConnection()
            updateState(state = Running(serverAddress, serverPort, visitorID))
        }
    }.transformFailedCause { cause ->
        when (cause) {
            is SecurityException -> throw cause
            else -> throw ConnectionFailedException(cause)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun SocketChannel.awaitConnection() {
        while (!finishConnect()) {
            yield()
        }
    }

    private suspend fun startClientJob(): Result<Unit> = applyCatching {
        coroutineScope {
            val clientSelector = clientSelector!!

            while (true) {
                if (clientSelector.isEventsNotOccurred) {
                    yield()
                    continue
                }

                val selectedKeys = clientSelector.selectedKeys
                val iterator = selectedKeys.iterator()

                while (iterator.hasNext()) {
                    val key = iterator.next().also { iterator.remove() }

                    if (key.isValid && key.isReadable) {
                        key.clientChannel?.receiveResponse()
                    }

                    if (key.isValid && key.isWritable) {
                        key.clientChannel?.sendVisitorID()
                    }

                    yield()
                }
            }
        }
    }

    private fun completeClientJob(): Result<Unit> = applyCatching {
        clientSelector?.safeClose()
        clientSocketChannel?.safeClose()
    }

    private fun SocketChannel.receiveResponse() {
        val readBytesCount = readResponse()

        when {
            readBytesCount < 0 -> throw BreakConnectionException(
                cause = IOException(/* message = */ "connection is broken")
            )

            readBytesCount == inputBufferSize -> {
                val ordinal = inputBuffer.int
                val serverResponse = ServerResponse.values().getOrNull(ordinal) ?: return

                when (serverResponse) {
                    ServerResponse.OK -> Unit

                    ServerResponse.BAD_REQUEST -> throw BadConnectionException(
                        cause = IOException(/* message = */ "bad connection")
                    )
                }
            }

            else -> throw BadConnectionException(
                cause = IOException(/* message = */ "bad connection")
            )
        }
    }

    private fun SocketChannel.sendVisitorID() {
        writeVisitorID()
    }

    private fun SocketChannel.readResponse(): Int {
        inputBuffer.clear()

        return try {
            read(inputBuffer).also { inputBuffer.flip() }
        } catch (exception: IOException) {
            throw DisconnectedException(cause = exception)
        }
    }

    private fun SocketChannel.writeVisitorID(): Int {
        outputBuffer.apply {
            clear()
            putLong(visitorID)
            flip()
        }

        return try {
            write(outputBuffer)
        } catch (exception: IOException) {
            throw DisconnectedException(cause = exception)
        }
    }

    private fun updateState(state: VisitorState) {
        _state.value = state
    }
}
