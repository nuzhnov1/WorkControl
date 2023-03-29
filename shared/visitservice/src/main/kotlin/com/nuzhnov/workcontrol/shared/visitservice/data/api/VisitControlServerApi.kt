package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.model.Visitor
import com.nuzhnov.workcontrol.core.visitcontrol.server.Server
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitControlServerApi {
    val visitors: Flow<Set<Visitor>>
    val serverState: StateFlow<ServerState>

    suspend fun startServer(address: InetAddress, port: Int = CONTROL_SERVER_DEFAULT_PORT)
    fun clearVisitors()

    companion object {
        const val CONTROL_SERVER_DEFAULT_PORT = Server.DEFAULT_PORT
    }
}
