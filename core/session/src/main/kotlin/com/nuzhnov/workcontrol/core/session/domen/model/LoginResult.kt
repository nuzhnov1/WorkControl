package com.nuzhnov.workcontrol.core.session.domen.model

sealed interface LoginResult {
    data class Success(val userData: UserData) : LoginResult

    sealed class Failure(val cause: Throwable) : LoginResult {
        class WrongLogin(cause: Throwable) : Failure(cause)
        class WrongPassword(cause: Throwable) : Failure(cause)
        class TooManyRequests(cause: Throwable) : Failure(cause)
        class ServiceError(cause: Throwable) : Failure(cause)
        class NetworkError(cause: Throwable) : Failure(cause)
        class UnknownError(cause: Throwable) : Failure(cause)
    }
}
