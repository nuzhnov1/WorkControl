package com.nuzhnov.workcontrol.common.visitcontrol.control

import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerError.*
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerException.InitException
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerException.MaxAcceptConnectionAttemptsReachedException
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState.*
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.util.*
import com.nuzhnov.workcontrol.common.util.applyCatching
import com.nuzhnov.workcontrol.common.util.applyUpdate
import com.nuzhnov.workcontrol.common.util.transformFailedCause
import kotlin.properties.Delegates
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
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan

internal class ControlServerImpl : ControlServer {

    private val _visits = MutableStateFlow(value = mapOf<VisitorID, Visit>())
    override val visits = _visits.map { visits -> visits.values.toSet() }

    private val _state = MutableStateFlow<ControlServerState>(value = NotRunningYet)
    override val state = _state.asStateFlow()

    private var serverJob: Job? = null

    private lateinit var address: InetAddress
    private var port by Delegates.notNull<Int>()
    private var backlog by Delegates.notNull<Int>()
    private var maxAcceptConnectionAttempts by Delegates.notNull<Int>()

    private val clientHandlers = mutableListOf<ClientHandler>()

    private var serverSelector: Selector? = null
    private var serverChannel: ServerSocketChannel? = null
    private var acceptConnectionAttempts = 0

    private val isStoppedAcceptConnections get() = _state.value is StoppedAcceptConnections
    private val isNoClients get() = clientHandlers.isEmpty()


    override suspend fun start(
        address: InetAddress?,
        port: Int,
        backlog: Int,
        maxAcceptConnectionAttempts: Int
    ): Unit = withContext(context = Dispatchers.IO) {
        initProperties(address, port, backlog, maxAcceptConnectionAttempts)
        serverJob?.cancelAndJoin()

        val newServerJob = launchServerJob()

        serverJob = newServerJob
        newServerJob.join()
    }

    override fun stop() {
        serverJob?.cancel()
    }

    override fun disconnectVisitor(visitorID: VisitorID) {
        synchronized(lock = clientHandlers) {
            clientHandlers.removeAll { handler ->
                if (handler.receivedVisitorID == visitorID) {
                    handler.stopHandlerJob()
                    true
                } else {
                    false
                }
            }
        }
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

    private fun initProperties(
        address: InetAddress?,
        port: Int,
        backlog: Int,
        maxAcceptConnectionAttempts: Int
    ) {
        if (maxAcceptConnectionAttempts < 0) {
            throw IllegalArgumentException("maxAcceptConnectionAttempts < 0")
        }

        if (address != null) {
            this.address = address
        }

        this.port = port
        this.backlog = backlog
        this.maxAcceptConnectionAttempts = maxAcceptConnectionAttempts
    }

    private fun CoroutineScope.launchServerJob(): Job = launch {
        val jobResult = initServer().fold(
            onSuccess = {
                startServerJob().fold(
                    onSuccess = { Result.success(Unit) },
                    onFailure = { cause -> Result.failure(cause) }
                )
            },

            onFailure = { cause -> Result.failure(cause) }
        )

        updateState(state = jobResult.toControlServerState())
        completeServerJob()
    }

    private fun Result<Unit>.toControlServerState(): ControlServerState = fold(
        onSuccess = { _state.value.toNextStateOnNormalCompletion() },
        onFailure = { cause ->
            when (cause) {
                is CancellationException -> _state.value.toNextStateOnNormalCompletion()
                is ControlServerException -> cause.toControlServerState()
                is IOException -> StoppedByError(address, port, error = IO_ERROR, cause)
                is SecurityException -> StoppedByError(address, port, error = SECURITY_ERROR, cause)
                else -> StoppedByError(address, port, error = UNKNOWN_ERROR, cause)
            }
        }
    )

    private fun ControlServerState.toNextStateOnNormalCompletion(): ControlServerState =
        when (this) {
            is Running                  -> Stopped(address, port)
            is StoppedAcceptConnections -> StoppedByError(address, port, error, cause)
            else                        -> this
        }

    private fun ControlServerException.toControlServerState(): ControlServerState =
        when (this) {
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

    private fun initServer(): Result<Unit> = applyCatching {
        serverSelector = Selector.open()
        serverChannel = ServerSocketChannel.open().apply {
            val socket = socket()
            val serverSocketAddress = when {
                ::address.isInitialized -> InetSocketAddress(address, port)
                else -> InetSocketAddress(port)
            }

            configureBlocking(/* block = */ false)
            socket.bind(serverSocketAddress, backlog)
            register(serverSelector!!, OP_ACCEPT)

            address = socket.inetAddress
            port = socket.localPort
            updateState(state = Running(address, port))
        }
    }.transformFailedCause { cause ->
        when (cause) {
            is SecurityException -> cause
            else -> InitException(cause)
        }
    }

    private suspend fun startServerJob(): Result<Unit> = applyCatching {
        coroutineScope {
            val serverSelector = serverSelector!!

            acceptConnectionAttempts = 0

            while (true) {
                if (isStoppedAcceptConnections && isNoClients) {
                    throw CancellationException()
                }

                if (serverSelector.isEventsNotOccurred) {
                    yield()
                    continue
                }

                val selectedKeys = serverSelector.selectedKeys
                val iterator = selectedKeys.iterator()

                while (iterator.hasNext()) {
                    val key = iterator.next().also { iterator.remove() }

                    if (!isStoppedAcceptConnections && key.isValid && key.isAcceptable) {
                        key.serverChannel?.apply {
                            acceptClient()
                                .onSuccess { handler -> onSuccessAcceptClient(handler) }
                                .onFailure { cause -> onFailureAcceptClient(cause) }
                        }
                    }

                    yield()
                }
            }
        }
    }

    private fun completeServerJob(): Result<Unit> = applyCatching {
        synchronized(lock = clientHandlers) {
            clientHandlers.forEach { it.stopHandlerJob() }
            clientHandlers.clear()
        }

        serverSelector?.safeClose()
        serverChannel?.safeClose()
    }

    private fun ServerSocketChannel.acceptClient(): Result<ClientHandler> = runCatching {
        val clientChannel = accept()
        val clientSelector = Selector.open()

        clientChannel.configureBlocking(/* block = */ false)
        clientChannel.register(clientSelector, /* ops = */ OP_READ or OP_WRITE)

        ClientHandler(
            controlServer = this@ControlServerImpl,
            selector = clientSelector,
            channel = clientChannel
        )
    }

    private fun CoroutineScope.onSuccessAcceptClient(clientHandler: ClientHandler) {
        acceptConnectionAttempts = 0

        clientHandler.apply {
            synchronized(lock = clientHandlers) { clientHandlers.add(this) }
            launchHandlerJob()
        }
    }

    private fun ServerSocketChannel.onFailureAcceptClient(cause: Throwable) {
        acceptConnectionAttempts++

        if (acceptConnectionAttempts > maxAcceptConnectionAttempts) {
            safeClose()

            updateState(
                state = StoppedAcceptConnections(
                    address = address,
                    port = port,
                    error = MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
                    cause = MaxAcceptConnectionAttemptsReachedException(cause)
                )
            )
        }
    }

    private fun updateState(state: ControlServerState) {
        _state.value = state
    }

    internal fun requestUpdateVisitorActivity(visitorID: VisitorID, isActiveNow: Boolean) {
        val visit = _visits.value[visitorID]

        if (visit == null || visit.isActivityChanged(isActiveNow)) {
            _visits.applyUpdate { updateVisitorActivity(visitorID, isActiveNow) }
        }
    }

    private fun MutableMap<VisitorID, Visit>.updateVisitorActivity(
        visitorID: VisitorID,
        isActiveNow: Boolean
    ) {
        val visit = this[visitorID]
        val now = DateTime.nowLocal()

        if (visit?.lastVisit == null) {
            this[visitorID] = Visit(
                visitorID = visitorID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { null },
                totalVisitDuration = TimeSpan.ZERO
            )
        } else {
            val isPreviouslyActive = visit.isActive
            val previouslyVisitTime = visit.lastVisit
            val duration = visit.totalVisitDuration
            val delta = now - previouslyVisitTime

            this[visitorID] = Visit(
                visitorID = visitorID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { previouslyVisitTime },
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }

    private fun Visit.isActivityChanged(isActiveNow: Boolean) = isActive != isActiveNow
}
