package com.nuzhnov.workcontrol.shared.visitservice.domen.model

sealed interface VisitorServiceState {
    object NotInitialized : VisitorServiceState
    object InitFailed : VisitorServiceState
    object ReadyToRun : VisitorServiceState
    object Discovering : VisitorServiceState
    data class Resolving(val serviceName: String) : VisitorServiceState
    object Connecting : VisitorServiceState
    object Running : VisitorServiceState
    object Stopped : VisitorServiceState
    data class StoppedByError(val error: VisitorServiceError) : VisitorServiceState
}
