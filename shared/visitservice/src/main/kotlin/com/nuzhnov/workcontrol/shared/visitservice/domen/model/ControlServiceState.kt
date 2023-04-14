package com.nuzhnov.workcontrol.shared.visitservice.domen.model

import java.net.InetAddress

sealed interface ControlServiceState {
    object NotInitialized : ControlServiceState
    object InitFailed : ControlServiceState
    object ReadyToRun : ControlServiceState

    data class Running internal constructor(
        internal val serverAddress: InetAddress,
        internal val serverPort: Int
    ) : ControlServiceState

    data class StoppedAcceptConnections(val error: ControlServiceError) : ControlServiceState
    object Stopped : ControlServiceState
    data class StoppedByError(val error: ControlServiceError) : ControlServiceState
}
