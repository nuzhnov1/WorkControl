package com.nuzhnov.workcontrol.core.controlservice.client

import com.nuzhnov.controlservice.data.api.client.ControlClientException.*
import com.nuzhnov.controlservice.data.api.client.ControlClientState.*
import com.nuzhnov.controlservice.data.api.client.ControlClientError.*
import com.nuzhnov.controlservice.data.api.model.ControlServerResponse
import com.nuzhnov.controlservice.data.api.common.*
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

    private val _clientState = MutableStateFlow<ControlClientState>(value = NotRunning)
    override val clientState get() = _clientState.asStateFlow()

    private var selector: Selector? = null
    private var clientSocketChannel: SocketChannel? = null

    private var clientID = 0L

    private val inputBufferSize = Int.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Long.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)


    override suspend fun start(serverAddress: InetAddress, serverPort: Int, clientID: Long) {
        this.clientID = clientID

        try {
            connect(serverAddress, serverPort)
                .onSuccess { runClient() }
                .onFailure { cause -> onConnectionFailed(cause) }
        } catch (exception: CancellationException) {
            _clientState.value = _clientState.value.toNextStateWhenCancelled()
        } catch (exception: ControlClientException) {
            _clientState.value = exception.toControlClientState()
        } catch (exception: IOException) {
            _clientState.value = Stopped(error = IO_ERROR, cause = exception)
        } catch (exception: SecurityException) {
            _clientState.value = Stopped(error = SECURITY_ERROR, cause = exception)
        } catch (exception: Throwable) {
            _clientState.value = Stopped(error = UNKNOWN_ERROR, cause = exception)
        } finally {
            finishClient()
        }
    }

    private fun ControlClientState.toNextStateWhenCancelled() = when (this) {
        is Running, is Connecting -> NotRunning
        else -> this
    }

    private fun ControlClientException.toControlClientState() = when (this) {
        is ConnectionFailedException -> Stopped(error = CONNECTION_FAILED, cause = cause)
        is BreakConnectionException -> Stopped(error = BREAK_CONNECTION, cause = cause)
        is BadConnectionException -> Stopped(error = BAD_CONNECTION, cause = cause)
        is ServerShutdownException -> NotRunning
    }

    private suspend fun connect(serverAddress: InetAddress, serverPort: Int) = runCatching {
        selector = Selector.open()
        clientSocketChannel = SocketChannel.open().apply {
            val selector = selector!!
            val serverSocketAddress = InetSocketAddress(serverAddress, serverPort)

            configureBlocking(/* block = */ false)
            connect(serverSocketAddress)
            register(selector, /* ops = */ OP_READ or OP_WRITE)

            _clientState.value = Connecting(
                serverAddress = serverAddress,
                serverPort = serverPort,
                clientID = clientID
            )

            awaitConnection()

            _clientState.value = Running(
                serverAddress = serverAddress,
                serverPort = serverPort,
                clientID = clientID
            )
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
            if (selector.isEventsNotOccurred) {
                yield()
                continue
            }

            val selectedKeys = selector.selectedKeys
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = iterator.next().also { iterator.remove() }

                if (key.isWritable) {
                    key.client?.onClientWrite()
                }

                if (key.isReadable) {
                    key.client?.onClientRead()
                }
            }
        }
    }

    private fun finishClient() = runCatching {
        runCatching { selector?.close() }
        runCatching { clientSocketChannel?.close() }

        Unit
    }

    private fun SocketChannel.onClientRead() {
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
        }
    }

    private fun SocketChannel.onClientWrite() {
        writeClientID()
    }

    private fun SocketChannel.readResponse(): Int {
        inputBuffer.clear()
        return read(inputBuffer)
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
