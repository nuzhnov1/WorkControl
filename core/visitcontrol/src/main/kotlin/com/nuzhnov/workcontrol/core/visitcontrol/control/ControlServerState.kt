package com.nuzhnov.workcontrol.core.visitcontrol.control

import java.net.InetAddress

sealed interface ControlServerState {
    object NotRunningYet : ControlServerState

    data class Running(val address: InetAddress, val port: Int) : ControlServerState

    data class StoppedAcceptConnections(
        val address: InetAddress,
        val port: Int,
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState

    data class Stopped(val address: InetAddress, val port: Int) : ControlServerState

    data class StoppedByError(
        val address: InetAddress,
        val port: Int,
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState
}
