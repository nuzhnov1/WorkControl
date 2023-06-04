package com.nuzhnov.workcontrol.common.visitcontrol.control

import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerError.*
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerException.InitException
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerException.MaxAcceptConnectionAttemptsReachedException
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState.*
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.util.isEventsNotOccurred
import com.nuzhnov.workcontrol.common.visitcontrol.util.safeClose
import com.nuzhnov.workcontrol.common.visitcontrol.util.selectedKeys
import com.nuzhnov.workcontrol.common.util.applyCatching
import com.nuzhnov.workcontrol.common.util.applyUpdate
import com.nuzhnov.workcontrol.common.util.transformFailedCause
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey.*
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan

internal class ControlServerImpl : ControlServer {

    private val _visits = MutableStateFlow(value = mapOf<VisitorID, Visit>())
    override val visits = _visits.map { visits -> visits.values.toSet() }

    private val _state = MutableStateFlow<ControlServerState>(value = NotRunningYet)
    override val state = _state.asStateFlow()


    private var serverSelector: Selector? = null
    private var serverChannel: ServerSocketChannel? = null
    private var clientHandlers: List<ClientHandler>? = null
    private var serverJob: Job? = null

    private var address: InetAddress? = null
    private var port: Int = 0

    override var backlog: Int = 512
        set(value) {
            if (value < 0) {
                throw IllegalArgumentException("backlog < 0")
            } else {
                field = value
            }
        }

    override var handlersCount: Int = 4
        set(value) {
            if (value < 1) {
                throw IllegalArgumentException("handlersCount < 0")
            } else {
                field = value
            }
        }

    override var maxAcceptConnectionAttempts: Int = 8
        set(value) {
            if (value < 0) {
                throw IllegalArgumentException("maxAcceptConnectionAttempts < 0")
            } else {
                field = value
            }
        }

    private var acceptConnectionAttempts: Int = 0


    private val isStoppedAcceptConnections: Boolean get() = _state.value is StoppedAcceptConnections



    override suspend fun start(): Unit = withContext(context = Dispatchers.IO) {
        serverJob?.cancelAndJoin()
        serverJob = coroutineContext.job

        val jobResult = initServer().fold(
            onSuccess = {
                doServerJob().fold(
                    onSuccess = { Result.success(Unit) },
                    onFailure = { cause -> Result.failure(cause) }
                )
            },

            onFailure = { cause -> Result.failure(cause) }
        )

        updateState(state = jobResult.toControlServerState())
        completeServerJob()
    }

    override suspend fun start(port: Int) {
        this.port = port

        start()
    }

    override suspend fun start(address: InetAddress, port: Int) {
        this.address = address
        this.port = port

        start()
    }

    override fun stop() {
        serverJob?.cancel()
    }

    override fun disconnectVisitor(visitorID: VisitorID) {
        clientHandlers?.forEach { handler -> handler.disconnectVisitor(visitorID) }
    }

    override fun updateVisits(vararg visit: Visit) {
        _visits.applyUpdate { putAll(visit.associateBy { it.visitorID }) }
    }

    override fun removeVisits(vararg visitorID: VisitorID) {
        _visits.applyUpdate { visitorID.forEach { remove(it) } }
    }

    override fun clearVisits() {
        _visits.applyUpdate { clear() }
    }

    private fun Result<Unit>.toControlServerState(): ControlServerState = fold(
        onSuccess = { _state.value.toNextStateOnNormalCompletion() },
        onFailure = { cause ->
            when (cause) {
                is CancellationException -> _state.value.toNextStateOnNormalCompletion()
                is ControlServerException -> cause.toControlServerState()
                is IOException -> StoppedByError(error = IO_ERROR, cause)
                is SecurityException -> StoppedByError(error = SECURITY_ERROR, cause)
                else -> StoppedByError(error = UNKNOWN_ERROR, cause)
            }
        }
    )

    private fun ControlServerState.toNextStateOnNormalCompletion(): ControlServerState =
        when (this) {
            is Running -> Stopped
            is StoppedAcceptConnections -> StoppedByError(error, cause)
            else -> this
        }

    private fun ControlServerException.toControlServerState(): ControlServerState =
        when (this) {
            is InitException -> StoppedByError(
                error = INIT_ERROR,
                cause = cause
            )

            is MaxAcceptConnectionAttemptsReachedException -> StoppedByError(
                error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
                cause = cause
            )
        }

    private fun initServer(): Result<Unit> = applyCatching {
        serverSelector = Selector.open()
        serverChannel = ServerSocketChannel.open().apply {
            val socket = socket()
            val serverSocketAddress = InetSocketAddress(address, port)

            configureBlocking(/* block = */ false)
            socket.bind(serverSocketAddress, backlog)
            register(serverSelector!!, OP_ACCEPT)

            address = socket.inetAddress
            port = socket.localPort
            updateState(state = Running(address!!, port))
        }

        clientHandlers = buildList(handlersCount) {
            repeat(handlersCount) { add(ClientHandler(controlServer = this@ControlServerImpl)) }
        }
    }.transformFailedCause { cause ->
        when (cause) {
            is SecurityException -> cause
            else -> InitException(cause)
        }
    }

    private suspend fun doServerJob(): Result<Unit> = applyCatching {
        coroutineScope {
            val serverSelector = serverSelector!!
            val serverChannel = serverChannel!!

            acceptConnectionAttempts = 0
            clientHandlers!!.forEach { handler -> launch { handler.start() } }

            while (!isStoppedAcceptConnections) {
                if (serverSelector.isEventsNotOccurred) {
                    yield()
                    continue
                }

                val selectedKeys = serverSelector.selectedKeys
                val iterator = selectedKeys.iterator()

                while (iterator.hasNext()) {
                    val key = iterator.next().also { iterator.remove() }

                    if (!isStoppedAcceptConnections && key.isValid && key.isAcceptable) {
                        serverChannel.acceptClient()
                            .onSuccess { channel -> onSuccessAcceptClient(channel) }
                            .onFailure { cause -> onFailureAcceptClient(cause) }
                    }

                    yield()
                }
            }
        }
    }

    private fun completeServerJob(): Result<Unit> = applyCatching {
        serverSelector?.safeClose()
        serverChannel?.safeClose()
        clientHandlers?.forEach { handler -> handler.stop() }

        serverSelector = null
        serverChannel = null
        clientHandlers = null
    }

    private fun ServerSocketChannel.acceptClient(): Result<SocketChannel> = runCatching { accept() }

    private fun onSuccessAcceptClient(clientChannel: SocketChannel) {
        acceptConnectionAttempts = 0
        clientHandlers!!
            .minBy { handler -> handler.connectionsCount }
            .attachClient(clientChannel)
            .onFailure { clientChannel.safeClose() }
    }

    private fun onFailureAcceptClient(cause: Throwable) {
        acceptConnectionAttempts++

        if (acceptConnectionAttempts > maxAcceptConnectionAttempts) {
            serverSelector?.safeClose()
            serverChannel?.safeClose()

            serverSelector = null
            serverChannel = null

            updateState(
                state = StoppedAcceptConnections(
                    error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
                    cause = MaxAcceptConnectionAttemptsReachedException(cause)
                )
            )
        }
    }

    private fun updateState(state: ControlServerState) {
        _state.value = state
    }

    internal fun updateVisitorActivity(visitorID: VisitorID, isActiveNow: Boolean) {
        val visit = _visits.value[visitorID]

        if (visit == null || visit.isActivityChanged(isActiveNow)) {
            _visits.applyUpdate { updateVisitorActivity(visitorID, isActiveNow) }
        }
    }

    internal fun makeAllVisitorsInactive() {
        _visits.applyUpdate {
            keys.forEach { visitorID -> updateVisitorActivity(visitorID, isActiveNow = false) }
        }
    }

    private fun MutableMap<VisitorID, Visit>.updateVisitorActivity(
        visitorID: VisitorID,
        isActiveNow: Boolean
    ) {
        val visit = this[visitorID]
        val now = DateTime.nowLocal()

        if (visit == null) {
            if (isActiveNow) {
                this[visitorID] = Visit(
                    visitorID = visitorID,
                    isActive = true,
                    lastVisit = now,
                    totalVisitDuration = TimeSpan.ZERO
                )
            }
        } else {
            val isPreviouslyActive = visit.isActive
            val previouslyVisitTime = visit.lastVisit
            val duration = visit.totalVisitDuration
            val delta = now - previouslyVisitTime

            this[visitorID] = Visit(
                visitorID = visitorID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) now else previouslyVisitTime,
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }

    private fun Visit.isActivityChanged(isActiveNow: Boolean): Boolean = isActive != isActiveNow
}
