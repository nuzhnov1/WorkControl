package com.nuzhnov.workcontrol.common.visitcontrol.visitor

import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorState.*
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorError.*
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorException.*
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.model.ServerResponse
import com.nuzhnov.workcontrol.common.visitcontrol.util.*
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

    private var job: Job? = null

    private var serverAddress by Delegates.notNull<InetAddress>()
    private var serverPort by Delegates.notNull<Int>()
    private var visitorID by Delegates.notNull<VisitorID>()

    private var selector: Selector? = null
    private var clientSocketChannel: SocketChannel? = null

    private val inputBufferSize = Int.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Long.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)


    override suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) = coroutineScope {
        initProperties(serverAddress, serverPort, visitorID)
        job?.cancelAndJoin()
        job = launch { startJob() }.also { job ->
            job.invokeOnCompletion { this@VisitorImpl.job = null }
            job.join()
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }

    private suspend fun startJob() {
        try {
            initiateConnection()
                .onSuccess { start() }
                .onFailure { cause -> throwExceptionAfterFailedConnection(cause) }
        } catch (exception: CancellationException) {
            _state.value = _state.value.toNextStateWhenCancelled()
        } catch (exception: VisitorException) {
            _state.value = exception.toVisitorState()
        } catch (exception: IOException) {
            _state.value = StoppedByError(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = visitorID,
                error = IO_ERROR,
                cause = exception
            )
        } catch (exception: SecurityException) {
            _state.value = StoppedByError(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = visitorID,
                error = SECURITY_ERROR,
                cause = exception
            )
        } catch (exception: Throwable) {
            _state.value = StoppedByError(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = visitorID,
                error = UNKNOWN_ERROR,
                cause = exception
            )
        } finally {
            finish()
        }
    }

    private fun initProperties(serverAddress: InetAddress, serverPort: Int, visitorID: VisitorID) {
        this.serverAddress = serverAddress
        this.serverPort = serverPort
        this.visitorID = visitorID
    }

    private fun VisitorState.toNextStateWhenCancelled() = when (this) {
        is Connecting -> Stopped(serverAddress, serverPort, visitorID)
        is Running -> Stopped(serverAddress, serverPort, visitorID)
        else -> this
    }

    private fun VisitorException.toVisitorState() = when (this) {
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

        is ServerShutdownException -> Stopped(serverAddress, serverPort, visitorID)
    }

    private suspend fun initiateConnection() = applyCatching {
        selector = Selector.open()
        clientSocketChannel = SocketChannel.open().apply {
            val selector = selector!!
            val socket = socket()
            val serverSocketAddress = InetSocketAddress(serverAddress, serverPort)

            configureBlocking(/* block = */ false)
            connect(serverSocketAddress)
            register(selector, /* ops = */ OP_READ or OP_WRITE)

            serverAddress = socket.inetAddress
            serverPort = socket.port

            _state.value = Connecting(serverAddress, serverPort, visitorID)
            awaitConnection()
            _state.value = Running(serverAddress, serverPort, visitorID)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun SocketChannel.awaitConnection() {
        while (!finishConnect()) {
            yield()
        }
    }

    private fun throwExceptionAfterFailedConnection(cause: Throwable): Nothing = when (cause) {
        is SecurityException -> throw cause
        else -> throw ConnectionFailedException(cause)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun start(): Nothing {
        val selector = selector!!

        while (true) {
            yield()

            if (selector.isEventsNotOccurred) {
                continue
            }

            val selectedKeys = selector.selectedKeys
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = iterator.next().also { iterator.remove() }

                if (!key.isValid) {
                    continue
                }

                if (key.isWritable) {
                    key.client?.sendVisitorID()
                }

                if (key.isReadable) {
                    key.client?.receiveResponse()
                }
            }
        }
    }

    private fun finish() = applyCatching {
        selector?.safeClose()
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

                    ServerResponse.DISCONNECTED -> throw DisconnectedException(
                        cause = IOException(/* message = */ "disconnected")
                    )

                    ServerResponse.SHUTDOWN_SERVER -> throw ServerShutdownException(
                        cause = IOException(/* message = */ "server shutdown")
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
        return read(inputBuffer).also { inputBuffer.flip() }
    }

    private fun SocketChannel.writeVisitorID(): Int {
        outputBuffer.apply {
            clear()
            putLong(visitorID)
            flip()
        }

        return write(outputBuffer)
    }
}
