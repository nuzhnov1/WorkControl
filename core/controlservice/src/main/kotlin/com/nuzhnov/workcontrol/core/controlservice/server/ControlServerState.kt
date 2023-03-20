package com.nuzhnov.workcontrol.core.controlservice.server

import java.net.InetAddress

sealed interface ControlServerState {
    object NotRunning : ControlServerState

    data class Running(
        val serverAddress: InetAddress,
        val serverPort: Int
    ) : ControlServerState

    data class StoppedAcceptConnections(
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState

    data class Stopped(
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState
}
