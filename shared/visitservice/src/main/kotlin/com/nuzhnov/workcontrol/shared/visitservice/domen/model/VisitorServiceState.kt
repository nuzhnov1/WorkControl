package com.nuzhnov.workcontrol.shared.visitservice.domen.model

sealed interface VisitorServiceState {
    object NotInitialized : VisitorServiceState
    data class InitFailed(val reason: VisitorServiceInitFailedReason) : VisitorServiceState
    object ReadyToStart : VisitorServiceState
    object Discovering : VisitorServiceState
    data class Resolving(val serviceName: String) : VisitorServiceState
    object NotRunningYet : VisitorServiceState
    object Connecting : VisitorServiceState
    object Running : VisitorServiceState
    object Stopped : VisitorServiceState
    data class StoppedByError(val error: VisitorServiceError) : VisitorServiceState
}
