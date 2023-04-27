package com.nuzhnov.workcontrol.common.visitcontrol.control

import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface ControlServer {
    val visits: Flow<Set<Visit>>
    val state: StateFlow<ControlServerState>


    suspend fun start(
        address: InetAddress? = null,
        port: Int = DEFAULT_PORT,
        backlog: Int = DEFAULT_BACKLOG,
        maxAcceptConnectionAttempts: Int = DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS
    )

    fun stop()

    fun disconnectVisitor(visitorID: VisitorID)

    fun updateVisits(vararg visit: Visit)

    fun removeVisits(vararg visitorID: VisitorID)

    fun clearVisits()


    companion object {
        const val DEFAULT_PORT = 0
        const val DEFAULT_BACKLOG = 512
        const val DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS = 16

        fun getDefaultControlServer(): ControlServer = ControlServerImpl()
    }
}
