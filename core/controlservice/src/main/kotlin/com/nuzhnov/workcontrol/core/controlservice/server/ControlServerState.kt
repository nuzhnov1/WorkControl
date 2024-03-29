package com.nuzhnov.workcontrol.core.controlservice.server

import java.net.InetAddress

sealed interface ControlServerState {
    object NotRunning : ControlServerState

    data class Running(
        val address: InetAddress,
        val port: Int
    ) : ControlServerState

    data class StoppedAcceptConnections(
        val address: InetAddress,
        val port: Int,
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState

    data class Stopped(
        val address: InetAddress,
        val port: Int,
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState
}
