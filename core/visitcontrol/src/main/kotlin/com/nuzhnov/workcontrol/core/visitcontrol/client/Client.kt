package com.nuzhnov.workcontrol.core.visitcontrol.client

import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface Client {
    val state: StateFlow<ClientState>

    suspend fun start(serverAddress: InetAddress, serverPort: Int, visitorID: Long)

    companion object {
        fun getDefaultClient(): Client = ClientImpl()
    }
}
