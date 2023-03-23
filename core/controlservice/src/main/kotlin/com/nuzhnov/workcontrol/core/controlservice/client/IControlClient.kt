package com.nuzhnov.workcontrol.core.controlservice.client

import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface IControlClient {
    val state: StateFlow<ControlClientState>

    suspend fun start(serverAddress: InetAddress, serverPort: Int, clientID: Long)

    companion object {
        fun getDefaultControlClient(): IControlClient = ControlClientImpl()
    }
}
