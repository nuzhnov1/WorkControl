package com.nuzhnov.workcontrol.common.visitcontrol.control

import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface ControlServer {
    val visits: Flow<Set<Visit>>
    val state: StateFlow<ControlServerState>

    var backlog: Int
    var handlersCount: Int
    var maxAcceptConnectionAttempts: Int


    suspend fun start()
    suspend fun start(port: Int)
    suspend fun start(address: InetAddress, port: Int)

    fun stop()

    fun disconnectVisitor(visitorID: VisitorID)

    fun updateVisits(vararg visit: Visit)

    fun removeVisits(vararg visitorID: VisitorID)

    fun clearVisits()


    companion object {
        fun getDefaultControlServer(): ControlServer = ControlServerImpl()
    }
}
