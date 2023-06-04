package com.nuzhnov.workcontrol.common.visitcontrol.control

import java.net.InetAddress

sealed interface ControlServerState {
    object NotRunningYet : ControlServerState

    data class Running(
        val address: InetAddress,
        val port: Int
    ) : ControlServerState

    data class StoppedAcceptConnections(
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState

    object Stopped : ControlServerState

    data class StoppedByError(
        val error: ControlServerError,
        val cause: Throwable
    ) : ControlServerState
}
