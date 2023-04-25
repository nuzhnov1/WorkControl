package com.nuzhnov.workcontrol.shared.studentservice.domen.model

sealed interface StudentServiceState {
    object NotInitialized : StudentServiceState
    object InitFailed : StudentServiceState
    object ReadyToRun : StudentServiceState
    object Discovering : StudentServiceState
    data class Resolving(val serviceName: String) : StudentServiceState
    object Connecting : StudentServiceState
    object Running : StudentServiceState
    object Stopped : StudentServiceState
    data class StoppedByError(val error: StudentServiceError) : StudentServiceState
}
