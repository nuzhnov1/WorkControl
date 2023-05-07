package com.nuzhnov.workcontrol.core.university.domen.model

sealed interface FetchStatus {
    object Success : FetchStatus

    sealed interface Failure : FetchStatus {
        object TooManyRequests : Failure
        object ServiceError : Failure
        object NetworkError: Failure
        object UnknownError: Failure
    }
}
