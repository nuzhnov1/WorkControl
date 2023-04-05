package com.nuzhnov.workcontrol.core.visitcontrol.visitor

import java.net.InetAddress

sealed interface VisitorState {
    object NotRunningYet : VisitorState

    data class Connecting(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: Long
    ) : VisitorState

    data class Running(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: Long
    ) : VisitorState

    data class Stopped(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: Long
    ) : VisitorState

    data class StoppedByError(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: Long,
        val error: VisitorError,
        val cause: Throwable
    ) : VisitorState
}
