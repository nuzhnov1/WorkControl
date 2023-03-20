package com.nuzhnov.controlservice.data.api.client

import java.net.InetAddress

sealed interface ControlClientState {
    object NotRunning : ControlClientState

    data class Connecting(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val clientID: Long
    ) : ControlClientState

    data class Running(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val clientID: Long
    ) : ControlClientState

    data class Stopped(
        val error: ControlClientError,
        val cause: Throwable
    ) : ControlClientState
}
