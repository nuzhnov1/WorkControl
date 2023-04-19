package com.nuzhnov.workcontrol.common.visitcontrol.control

import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState.*
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerError.*
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerException.*
import com.nuzhnov.workcontrol.common.visitcontrol.model.*
import com.nuzhnov.workcontrol.common.visitcontrol.util.*
import kotlin.properties.Delegates
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.*
import java.nio.channels.SelectionKey.*
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan

internal class ControlServerImpl : ControlServer {

    private val _visits = MutableStateFlow(value = mapOf<VisitorID, Visit>())
    override val visits = _visits.map { visits -> visits.values.toSet() }

    private val _state = MutableStateFlow<ControlServerState>(value = NotRunningYet)
    override val state = _state.asStateFlow()

    private var job: Job? = null

    private lateinit var address: InetAddress
    private var port by Delegates.notNull<Int>()
    private var backlog by Delegates.notNull<Int>()
    private var maxAcceptConnectionAttempts by Delegates.notNull<Int>()

    private val visitorsConnections = mutableMapOf<SocketAddress, VisitorConnection>()
    private var selector: Selector? = null
    private var serverSocketChannel: ServerSocketChannel? = null
    private var acceptConnectionAttempts = 0

    private val inputBufferSize = Long.SIZE_BYTES
    private val inputBuffer = ByteBuffer.allocate(inputBufferSize)
    private val outputBufferSize = Int.SIZE_BYTES
    private val outputBuffer = ByteBuffer.allocate(outputBufferSize)

    private val isStoppedAcceptConnections get() = _state.value is StoppedAcceptConnections
    private val isNoClients get() = visitorsConnections.isEmpty()


    override suspend fun start(
        address: InetAddress,
        port: Int,
        backlog: Int,
        maxAcceptConnectionAttempts: Int
    ) = coroutineScope {
        initProperties(address, port, backlog, maxAcceptConnectionAttempts)
        job?.cancelAndJoin()
        job = launch { startJob() }.also { job ->
            job.invokeOnCompletion { this@ControlServerImpl.job = null }
            job.join()
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }

    override fun disconnectVisitor(visitorID: VisitorID) {
        visitorsConnections.values
            .filter { (_, id) -> id == visitorID }
            .forEach { (channel, _) -> channel.disconnectVisitor(ServerResponse.DISCONNECTED) }
    }

    override fun updateVisits(vararg visit: Visit) {
        _visits.applyUpdate { putAll(visit.associateBy { it.visitorID }) }
    }

    override fun clearVisits() {
        _visits.applyUpdate { clear() }
    }

    private suspend fun startJob() {
        try {
            init()
                .onSuccess { start() }
                .onFailure { cause -> throwExceptionAfterInitFailed(cause) }
        } catch (exception: CancellationException) {
            _state.value = _state.value.toNextStateWhenCancelOccurs()
        } catch (exception: ControlServerException) {
            _state.value = exception.toControlServerState()
        } catch (exception: IOException) {
            _state.value = StoppedByError(address, port, error = IO_ERROR, cause = exception)
        } catch (exception: SecurityException) {
            _state.value = StoppedByError(address, port, error = SECURITY_ERROR, cause = exception)
        } catch (exception: Throwable) {
            _state.value = StoppedByError(address, port, error = UNKNOWN_ERROR, cause =  exception)
        } finally {
            finish()
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

    private fun ControlServerState.toNextStateWhenCancelOccurs() = when (this) {
        is Running -> Stopped(address, port)
        is StoppedAcceptConnections -> StoppedByError(address, port, error, cause)
        else -> this
    }

    private fun ControlServerException.toControlServerState() = when (this) {
        is InitException -> StoppedByError(
            address = address,
            port = port,
            error = INIT_ERROR,
            cause = cause
        )

        is MaxAcceptConnectionAttemptsReachedException -> StoppedByError(
            address = address,
            port = port,
            error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
            cause = cause
        )
    }

    private fun init() = applyCatching {
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

    private fun throwExceptionAfterInitFailed(cause: Throwable): Nothing = when (cause) {
        is SecurityException -> throw cause
        else -> throw InitException(cause)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun start(): Nothing {
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

                if (!key.isValid) {
                    continue
                }

                if (key.isAcceptable) {
                    key.server?.apply {
                        acceptVisitor(selector)
                            .onSuccess { acceptConnectionAttempts = 0 }
                            .onFailure { cause -> onFailedAcceptVisitor(cause) }
                    }
                }

                if (key.isReadable) {
                    key.client?.apply {
                        receiveVisitorID()
                            .onFailure { disconnectVisitor() }
                    }
                }
            }
        }
    }

    private fun finish() = applyCatching {
        visitorsConnections.disconnectAll()
        selector?.safeClose()
        serverSocketChannel?.safeClose()
    }

    private fun ServerSocketChannel.acceptVisitor(selector: Selector) = applyCatching {
        val clientChannel = accept()
        val clientSocket = clientChannel.socket()
        val clientAddress = clientSocket.remoteSocketAddress

        clientChannel.configureBlocking(/* block = */ false)
        clientChannel.register(selector, /* ops = */ OP_READ or OP_WRITE)

        visitorsConnections[clientAddress] = VisitorConnection(channel = clientChannel, id = null)
    }

    private fun ServerSocketChannel.onFailedAcceptVisitor(cause: Throwable) {
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
        val visitorID = visitorsConnections[clientAddress]?.id

        val readBytesCount = readVisitorID()

        when {
            readBytesCount < 0 -> disconnectVisitor()

            readBytesCount == inputBufferSize -> {
                val id = inputBuffer.long

                if (visitorID == null) {
                    visitorsConnections[clientAddress] = VisitorConnection(channel = this, id = id)
                    updateVisitorActivity(visitorID = id, isActiveNow = true)
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

    private fun SocketChannel.disconnectVisitor(serverResponse: ServerResponse? = null) {
        val clientSocket = socket()
        val clientAddress = clientSocket.remoteSocketAddress

        visitorsConnections.remove(clientAddress)?.also { (_, visitorID) ->
            if (visitorID != null) {
                updateVisitorActivity(visitorID, isActiveNow = false)
            }
        }

        if (serverResponse != null) {
            safeWriteResponse(serverResponse)
        }

        safeClose()
    }

    private fun MutableMap<SocketAddress, VisitorConnection>.disconnectAll() {
        makeAllVisitorsInactive()
        values.forEach { (channel, _) ->
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

    private fun updateVisitorActivity(visitorID: VisitorID, isActiveNow: Boolean) {
        val visit = _visits.value[visitorID]

        if (visit == null || visit.isActivityChanged(isActiveNow)) {
            _visits.applyUpdate { updateVisitorActivity(visitorID, isActiveNow) }
        }
    }

    private fun makeAllVisitorsInactive() {
        _visits.applyUpdate {
            keys.forEach { id -> updateVisitorActivity(visitorID = id, isActiveNow = false) }
        }
    }

    private fun MutableStateFlow<Map<VisitorID, Visit>>.applyUpdate(
        block: MutableMap<VisitorID, Visit>.() -> Unit
    ) {
        value = value.toMutableMap().apply(block)
    }

    private fun MutableMap<VisitorID, Visit>.updateVisitorActivity(
        visitorID: VisitorID,
        isActiveNow: Boolean
    ) {
        val visit = this[visitorID]
        val now = DateTime.nowLocal()

        if (visit?.lastVisitDateTime == null) {
            this[visitorID] = Visit(
                visitorID = visitorID,
                isActive = isActiveNow,
                lastVisitDateTime = if (isActiveNow) { now } else { null },
                totalVisitDuration = TimeSpan.ZERO
            )
        } else {
            val isPreviouslyActive = visit.isActive
            val previouslyVisitTime = visit.lastVisitDateTime
            val duration = visit.totalVisitDuration
            val delta = now - previouslyVisitTime

            this[visitorID] = Visit(
                visitorID = visitorID,
                isActive = isActiveNow,
                lastVisitDateTime = if (isActiveNow) { now } else { previouslyVisitTime },
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }

    private fun Visit.isActivityChanged(isActiveNow: Boolean) = isActive != isActiveNow


    private data class VisitorConnection(val channel: SocketChannel, val id: VisitorID?)
}
