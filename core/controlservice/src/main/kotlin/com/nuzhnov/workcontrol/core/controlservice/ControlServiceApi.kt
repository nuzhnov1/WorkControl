package com.nuzhnov.workcontrol.core.controlservice

import com.nuzhnov.workcontrol.core.controlservice.server.IControlServer
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerState
import com.nuzhnov.workcontrol.core.controlservice.client.IControlClient
import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientState
import com.nuzhnov.workcontrol.core.controlservice.model.ClientApiModel
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface ControlServiceApi {

    val server: IControlServer
    val client: IControlClient

    val clients: StateFlow<Set<ClientApiModel>> get() = server.clients
    val serverState: StateFlow<ControlServerState> get() = server.state

    val clientState: StateFlow<ControlClientState> get() = client.state


    suspend fun startServer(
        address: InetAddress,
        port: Int = IControlServer.DEFAULT_PORT,
        backlog: Int = IControlServer.DEFAULT_BACKLOG,
        maxAcceptConnectionAttempts: Int = IControlServer.DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS
    ) {
        server.start(address, port, backlog, maxAcceptConnectionAttempts)
    }

    suspend fun startClient(serverAddress: InetAddress, serverPort: Int, clientID: Long) {
        client.start(serverAddress, serverPort, clientID)
    }


    companion object {
        fun getDefaultControlServer(): ControlServiceApi = ControlServiceApiImpl()
    }
}
