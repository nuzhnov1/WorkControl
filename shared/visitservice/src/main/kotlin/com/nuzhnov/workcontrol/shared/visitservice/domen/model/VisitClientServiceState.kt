package com.nuzhnov.workcontrol.shared.visitservice.domen.model

sealed interface VisitClientServiceState {
    object NotCreated : VisitClientServiceState
    object Initialized : VisitClientServiceState
    object NotRunning : VisitClientServiceState
    object Connecting : VisitClientServiceState
    object Running : VisitClientServiceState
    data class Stopped(val reason: VisitClientServiceStopReason) : VisitClientServiceState
}
