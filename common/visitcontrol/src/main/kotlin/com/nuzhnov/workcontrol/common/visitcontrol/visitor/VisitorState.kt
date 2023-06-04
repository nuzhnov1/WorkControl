package com.nuzhnov.workcontrol.common.visitcontrol.visitor

import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import java.net.InetAddress

sealed interface VisitorState {
    object NotRunningYet : VisitorState

    data class Connecting(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: VisitorID
    ) : VisitorState

    data class Running(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val visitorID: VisitorID
    ) : VisitorState

    object Stopped : VisitorState

    data class StoppedByError(
        val error: VisitorError,
        val cause: Throwable
    ) : VisitorState
}
