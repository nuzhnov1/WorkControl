package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientState
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitControlClientApi {
    val clientState: StateFlow<ClientState>

    suspend fun startClient(serverAddress: InetAddress, serverPort: Int, visitorID: Long)
}
