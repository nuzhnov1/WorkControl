package com.nuzhnov.workcontrol.shared.visitservice.domen.model

import java.net.InetAddress

sealed interface ControlServiceState {
    object NotInitialized : ControlServiceState
    data class InitFailed(val reason: ControlServiceInitFailedReason) : ControlServiceState
    object ReadyToStart : ControlServiceState
    object NotRunningYet : ControlServiceState

    data class Running internal constructor(
        internal val serverAddress: InetAddress,
        internal val serverPort: Int
    ) : ControlServiceState

    data class StoppedAcceptConnections(val error: ControlServiceError) : ControlServiceState
    object Stopped : ControlServiceState
    data class StoppedByError(val error: ControlServiceError) : ControlServiceState
}
