package com.nuzhnov.workcontrol.shared.visitcontrol.data.model

sealed interface ServiceState {
    object Initialized : ServiceState
    object Running : ServiceState
    data class Stopped(val stopReason: StopReason) : ServiceState
}
