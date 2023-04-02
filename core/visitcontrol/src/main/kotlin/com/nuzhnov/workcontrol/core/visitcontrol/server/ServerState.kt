package com.nuzhnov.workcontrol.core.visitcontrol.server

import java.net.InetAddress

sealed interface ServerState {
    object NotRunning : ServerState

    data class Running(
        val address: InetAddress,
        val port: Int
    ) : ServerState

    data class StoppedAcceptConnections(
        val address: InetAddress,
        val port: Int,
        val error: ServerError,
        val cause: Throwable
    ) : ServerState

    data class Stopped(
        val address: InetAddress,
        val port: Int
    ) : ServerState

    data class StoppedByError(
        val address: InetAddress,
        val port: Int,
        val error: ServerError,
        val cause: Throwable
    ) : ServerState
}
