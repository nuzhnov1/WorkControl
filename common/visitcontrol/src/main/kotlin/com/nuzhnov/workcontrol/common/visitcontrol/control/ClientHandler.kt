package com.nuzhnov.workcontrol.common.visitcontrol.control

import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.model.ServerResponse
import com.nuzhnov.workcontrol.common.visitcontrol.util.*
import kotlinx.coroutines.*
import java.nio.ByteBuffer
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

internal class ClientHandler(
    internal val controlServer: ControlServerImpl,
    internal val selector: Selector,
    internal val channel: SocketChannel
) {

    internal var receivedVisitorID: VisitorID? = null
        private set

    private var handlerJob: Job? = null

    private val inputBufferSize = Long.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Int.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)


    internal fun CoroutineScope.launchHandlerJob(): Job {
        val job = launch {
            startHandleJob()
            completeHandlerJob()
        }

        handlerJob = job
        return job
    }

    internal fun stopHandlerJob(serverResponse: ServerResponse) {
        channel.safeWriteResponse(serverResponse)
        handlerJob?.cancel()
    }

    private suspend fun startHandleJob(): Result<Unit> = applyCatching {
        while (true) {
            if (selector.isEventsNotOccurred) {
                yield()
                continue
            }

            val selectedKeys = selector.selectedKeys
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = iterator.next().also { iterator.remove() }

                if (key.isValid && key.isReadable) {
                    key.clientChannel?.receiveVisitorID()
                }

                yield()
            }
        }
    }

    private fun completeHandlerJob() {
        selector.safeClose()
        channel.safeClose()

        controlServer.requestUpdateVisitorActivity(
            visitorID = receivedVisitorID ?: return,
            isActiveNow = false
        )
    }

    private fun SocketChannel.receiveVisitorID() {
        val readBytesCount = readVisitorID()

        when {
            readBytesCount < 0 -> throw CancellationException()

            readBytesCount == inputBufferSize -> {
                val id = inputBuffer.long

                if (receivedVisitorID == null) {
                    receivedVisitorID = id

                    controlServer.requestUpdateVisitorActivity(
                        visitorID = id,
                        isActiveNow = true
                    )
                } else {
                    controlServer.requestUpdateVisitorActivity(
                        visitorID = receivedVisitorID!!,
                        isActiveNow = true
                    )
                }

                writeResponse(ServerResponse.OK)
            }

            else -> {
                if (receivedVisitorID != null) {
                    controlServer.requestUpdateVisitorActivity(
                        visitorID = receivedVisitorID!!,
                        isActiveNow = false
                    )
                }

                writeResponse(ServerResponse.BAD_REQUEST)
            }
        }
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
}
