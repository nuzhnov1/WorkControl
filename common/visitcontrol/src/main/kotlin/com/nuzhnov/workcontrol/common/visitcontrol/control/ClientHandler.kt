package com.nuzhnov.workcontrol.common.visitcontrol.control

import com.nuzhnov.workcontrol.common.visitcontrol.model.ServerResponse
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.util.clientChannel
import com.nuzhnov.workcontrol.common.visitcontrol.util.isEventsNotOccurred
import com.nuzhnov.workcontrol.common.visitcontrol.util.safeClose
import com.nuzhnov.workcontrol.common.visitcontrol.util.selectedKeys
import com.nuzhnov.workcontrol.common.util.applyCatching
import kotlinx.coroutines.*
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SelectionKey.OP_WRITE
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

internal class ClientHandler(private val controlServer: ControlServerImpl) {

    private var clientSelector: Selector? = null
    private val clientConnections = mutableMapOf<SocketChannel, VisitorID>()
    private var handlerJob: Job? = null

    private val inputBufferSize = Long.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Int.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)

    internal val connectionsCount get() = synchronized(lock = clientConnections) {
        clientConnections.size
    }


    internal suspend fun start() = coroutineScope {
        handlerJob?.cancelAndJoin()
        handlerJob = coroutineContext.job

        doHandlerJob()
        completeHandlerJob()
    }

    internal fun stop() {
        handlerJob?.cancel()
    }

    internal fun attachClient(channel: SocketChannel) = applyCatching {
        val clientSelector = requireNotNull(clientSelector)

        channel.configureBlocking(/* block = */ false)
        channel.register(clientSelector, /* ops = */ OP_READ or OP_WRITE)
    }

    internal fun disconnectVisitor(id: VisitorID) = synchronized(lock = clientConnections) {
        clientConnections
            .filter { (_, visitorID) -> visitorID == id }
            .keys
            .forEach { channel ->
                channel.safeClose()
                clientConnections.remove(channel)
            }

        controlServer.updateVisitorActivity(visitorID = id, isActiveNow = false)
    }

    private suspend fun doHandlerJob() = applyCatching {
        val selector = requireNotNull(Selector.open()).apply { clientSelector = this }

        while (true) {
            if (selector.isEventsNotOccurred) {
                yield()
                continue
            }

            val selectedKeys = selector.selectedKeys
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = requireNotNull(iterator.next()).also { iterator.remove() }

                if (key.isValid && key.isReadable) {
                    val channel = key.clientChannel ?: continue
                    channel.receiveVisitorID().onFailure { channel.closeConnection() }
                }

                yield()
            }
        }
    }

    private fun completeHandlerJob() {
        clientSelector?.safeClose()
        clientSelector = null

        closeAllConnections()
    }

    private fun SocketChannel.closeConnection() = synchronized(lock = clientConnections) {
        safeClose()
        clientConnections.remove(key = this)?.let { visitorID ->
            controlServer.updateVisitorActivity(visitorID, isActiveNow = false)
        }
    }

    private fun closeAllConnections() = synchronized(lock = clientConnections) {
        clientConnections.keys.forEach { channel -> channel.safeClose() }
        clientConnections.clear()
        controlServer.makeAllVisitorsInactive()
    }

    private fun SocketChannel.receiveVisitorID() = applyCatching {
        val oldID = synchronized(lock = clientConnections) { clientConnections[this] }
        val readBytesCount = readVisitorID()

        when {
            readBytesCount < 0 -> throw CancellationException()

            readBytesCount == inputBufferSize -> {
                val receivedID = inputBuffer.long

                if (oldID == null) {
                    synchronized(lock = clientConnections) { clientConnections[this] = receivedID }

                    controlServer.updateVisitorActivity(
                        visitorID = receivedID,
                        isActiveNow = true
                    )
                } else {
                    controlServer.updateVisitorActivity(
                        visitorID = oldID,
                        isActiveNow = true
                    )
                }

                writeResponse(ServerResponse.OK)
            }

            else -> {
                if (oldID != null) {
                    controlServer.updateVisitorActivity(
                        visitorID = oldID,
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
}
