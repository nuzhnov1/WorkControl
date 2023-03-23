package com.nuzhnov.workcontrol.core.controlservice.client

import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientException.*
import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientState.*
import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientError.*
import com.nuzhnov.workcontrol.core.controlservice.model.ControlServerResponse
import com.nuzhnov.workcontrol.core.controlservice.common.*
import kotlin.properties.Delegates
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.yield
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey.*
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

internal class ControlClientImpl : IControlClient {

    private val _state = MutableStateFlow<ControlClientState>(value = NotRunning)
    override val state = _state.asStateFlow()

    private var serverAddress by Delegates.notNull<InetAddress>()
    private var serverPort by Delegates.notNull<Int>()
    private var clientID by Delegates.notNull<Long>()

    private var selector: Selector? = null
    private var clientSocketChannel: SocketChannel? = null

    private val inputBufferSize = Int.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Long.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)


    override suspend fun start(serverAddress: InetAddress, serverPort: Int, clientID: Long) {
        initProperties(serverAddress, serverPort, clientID)

        try {
            initiateConnection()
                .onSuccess { runClient() }
                .onFailure { cause -> onConnectionFailed(cause) }
        } catch (exception: CancellationException) {
            _state.value = _state.value.toNextStateWhenCancelled()
        } catch (exception: ControlClientException) {
            _state.value = exception.toControlClientState()
        } catch (exception: IOException) {
            _state.value = Stopped(
                serverAddress = serverAddress,
                serverPort = serverPort,
                clientID = clientID,
                error = IO_ERROR,
                cause = exception
            )
        } catch (exception: SecurityException) {
            _state.value = Stopped(
                serverAddress = serverAddress,
                serverPort = serverPort,
                clientID = clientID,
                error = SECURITY_ERROR,
                cause = exception
            )
        } catch (exception: Throwable) {
            _state.value = Stopped(
                serverAddress = serverAddress,
                serverPort = serverPort,
                clientID = clientID,
                error = UNKNOWN_ERROR,
                cause = exception
            )
        } finally {
            finishClient()
        }
    }

    private fun initProperties(serverAddress: InetAddress, serverPort: Int, clientID: Long) {
        this.serverAddress = serverAddress
        this.serverPort = serverPort
        this.clientID = clientID
    }

    private fun ControlClientState.toNextStateWhenCancelled() = when (this) {
        is Running, is Connecting -> NotRunning
        else -> this
    }

    private fun ControlClientException.toControlClientState() = when (this) {
        is ConnectionFailedException -> Stopped(
            serverAddress = serverAddress,
            serverPort = serverPort,
            clientID = clientID,
            error = CONNECTION_FAILED,
            cause = cause
        )

        is BreakConnectionException -> Stopped(
            serverAddress = serverAddress,
            serverPort = serverPort,
            clientID = clientID,
            error = BREAK_CONNECTION,
            cause = cause
        )

        is BadConnectionException -> Stopped(
            serverAddress = serverAddress,
            serverPort = serverPort,
            clientID = clientID,
            error = BAD_CONNECTION,
            cause = cause
        )

        is ServerShutdownException -> NotRunning
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

            _state.value = Connecting(serverAddress, serverPort, clientID)
            awaitConnection()
            _state.value = Running(serverAddress, serverPort, clientID)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun SocketChannel.awaitConnection() {
        while (!finishConnect()) {
            yield()
        }
    }

    private fun onConnectionFailed(cause: Throwable): Nothing = when (cause) {
        is SecurityException -> throw cause
        else -> throw ConnectionFailedException(cause)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun runClient(): Nothing {
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

                if (key.isWritable) {
                    key.client?.sendID()
                }

                if (key.isReadable) {
                    key.client?.receiveResponse()
                }
            }
        }
    }

    private fun finishClient() = applyCatching {
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
                val serverResponse = ControlServerResponse.values().getOrNull(ordinal) ?: return

                when (serverResponse) {
                    ControlServerResponse.OK -> Unit

                    ControlServerResponse.BAD_REQUEST -> throw BadConnectionException(
                        cause = IOException(/* message = */ "bad connection")
                    )

                    ControlServerResponse.SHUTDOWN_SERVER -> throw ServerShutdownException(
                        cause = IOException(/* message = */ "server shutdown")
                    )
                }
            }

            else -> throw BadConnectionException(
                cause = IOException(/* message = */ "bad connection")
            )
        }
    }

    private fun SocketChannel.sendID() {
        writeClientID()
    }

    private fun SocketChannel.readResponse(): Int {
        inputBuffer.clear()
        return read(inputBuffer).also { inputBuffer.flip() }
    }

    private fun SocketChannel.writeClientID(): Int {
        outputBuffer.apply {
            clear()
            putLong(clientID)
            flip()
        }

        return write(outputBuffer)
    }
}
