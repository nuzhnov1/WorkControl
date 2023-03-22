package com.nuzhnov.workcontrol.core.controlservice.client

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
        val serverAddress: InetAddress,
        val serverPort: Int,
        val clientID: Long,
        val error: ControlClientError,
        val cause: Throwable
    ) : ControlClientState
}
