package com.nuzhnov.workcontrol.core.visitcontrol.client

import java.net.InetAddress

sealed interface ClientState {
    object NotRunning : ClientState

    data class Connecting(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: Long
    ) : ClientState

    data class Running(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: Long
    ) : ClientState

    data class Stopped(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: Long,
        val error: ClientError,
        val cause: Throwable
    ) : ClientState
}
