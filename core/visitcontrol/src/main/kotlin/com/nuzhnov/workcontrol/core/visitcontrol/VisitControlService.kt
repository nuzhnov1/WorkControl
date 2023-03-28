package com.nuzhnov.workcontrol.core.visitcontrol

import com.nuzhnov.workcontrol.core.visitcontrol.server.IServer
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerState
import com.nuzhnov.workcontrol.core.visitcontrol.client.IClient
import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientState
import com.nuzhnov.workcontrol.core.visitcontrol.model.Visitor
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

abstract class VisitControlService {

    protected abstract val controlServer: IServer
    protected abstract val client: IClient

    val visitors: StateFlow<Set<Visitor>> get() = controlServer.visitors
    val controlServerState: StateFlow<ServerState> get() = controlServer.state
    val clientState: StateFlow<ClientState> get() = client.state


    suspend fun startControlServer(
        address: InetAddress,
        port: Int = IServer.DEFAULT_PORT,
        backlog: Int = IServer.DEFAULT_BACKLOG,
        maxAcceptConnectionAttempts: Int = IServer.DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS
    ) {
        controlServer.start(address, port, backlog, maxAcceptConnectionAttempts)
    }

    suspend fun startClient(serverAddress: InetAddress, serverPort: Int, visitorID: Long) {
        client.start(serverAddress, serverPort, visitorID)
    }


    companion object {
        fun getDefaultControlServer(): VisitControlService = VisitControlServiceImpl()
    }
}
