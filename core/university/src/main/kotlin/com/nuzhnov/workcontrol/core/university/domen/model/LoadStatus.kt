package com.nuzhnov.workcontrol.core.university.domen.model

sealed interface LoadStatus {
    object Success : LoadStatus

    sealed interface Failure : LoadStatus {
        object TooManyRequests : Failure
        object ServiceError : Failure
        object NetworkError: Failure
        object UnknownError: Failure
    }
}
