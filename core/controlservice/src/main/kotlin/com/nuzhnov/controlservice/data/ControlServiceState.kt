package com.nuzhnov.controlservice.data

sealed interface ControlServiceState {
    object Initialized : ControlServiceState
    object Running : ControlServiceState
    data class Stopped(val stopReason: StopReason) : ControlServiceState
}
