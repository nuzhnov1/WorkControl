package com.nuzhnov.controlservice.data.api

import com.nuzhnov.controlservice.data.api.server.IControlServer
import com.nuzhnov.controlservice.data.api.server.ControlServerState
import com.nuzhnov.controlservice.data.api.client.IControlClient
import com.nuzhnov.controlservice.data.api.client.ControlClientState
import com.nuzhnov.controlservice.data.api.model.ClientApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface ControlServiceApi {

    val server: IControlServer
    val client: IControlClient

    val clients: Flow<List<ClientApiModel>> get() = server.clients
    val serverState: StateFlow<ControlServerState> get() = server.serverState

    val clientState: StateFlow<ControlClientState> get() = client.clientState


    suspend fun startServer(
        serverAddress: InetAddress,
        backlog: Int = IControlServer.DEFAULT_BACKLOG,
        maxAcceptConnectionAttempts: Int = IControlServer.DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS
    ) {
        server.start(serverAddress, backlog, maxAcceptConnectionAttempts)
    }

    suspend fun startClient(serverAddress: InetAddress, serverPort: Int, clientID: Long) {
        client.start(serverAddress, serverPort, clientID)
    }


    companion object {
        fun getDefaultControlServer(): ControlServiceApi = ControlServiceApiImpl()
    }
}
