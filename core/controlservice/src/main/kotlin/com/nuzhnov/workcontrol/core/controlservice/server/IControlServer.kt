package com.nuzhnov.workcontrol.core.controlservice.server

import com.nuzhnov.workcontrol.core.controlservice.model.ClientApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface IControlServer {
    val clients: Flow<List<ClientApiModel>>
    val serverState: StateFlow<ControlServerState>

    suspend fun start(
        serverAddress: InetAddress,
        backlog: Int = DEFAULT_BACKLOG,
        maxAcceptConnectionAttempts: Int = DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS
    )

    companion object {
        const val DEFAULT_BACKLOG = 512
        const val DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS = 16

        fun getDefaultControlServer(): IControlServer = ControlServerImpl()
    }
}
