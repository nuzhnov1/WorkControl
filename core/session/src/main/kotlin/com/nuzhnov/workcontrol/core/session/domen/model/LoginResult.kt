package com.nuzhnov.workcontrol.core.session.domen.model

sealed interface LoginResult {
    data class Success(val userData: UserData) : LoginResult

    sealed interface Failure : LoginResult {
        object WrongLogin : Failure
        object WrongPassword : Failure
        object TooManyRequests : Failure
        object ServiceError : Failure
        object NetworkError : Failure
        object UnknownError : Failure
    }
}
