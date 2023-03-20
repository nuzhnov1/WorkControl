package com.nuzhnov.controlservice.data.api.client

import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface IControlClient {
    val clientState: StateFlow<ControlClientState>

    suspend fun start(serverAddress: InetAddress, serverPort: Int, clientID: Long)

    companion object {
        fun getDefaultControlClient(): IControlClient = ControlClientImpl()
    }
}
