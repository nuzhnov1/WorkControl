package com.nuzhnov.workcontrol.shared.visitservice.domen.model

sealed interface VisitControlServiceState {
    object NotCreated : VisitControlServiceState
    object Initialized : VisitControlServiceState
    object NotRunning : VisitControlServiceState
    object Running : VisitControlServiceState
    object StoppedAcceptConnections : VisitControlServiceState
    data class Stopped(val reason: VisitControlServiceStopReason) : VisitControlServiceState
}
