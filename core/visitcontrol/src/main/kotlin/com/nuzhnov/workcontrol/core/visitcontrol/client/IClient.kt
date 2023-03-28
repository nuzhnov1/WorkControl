package com.nuzhnov.workcontrol.core.visitcontrol.client

import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface IClient {
    val state: StateFlow<ClientState>

    suspend fun start(serverAddress: InetAddress, serverPort: Int, visitorID: Long)

    companion object {
        fun getDefaultControlClient(): IClient = ClientImpl()
    }
}
