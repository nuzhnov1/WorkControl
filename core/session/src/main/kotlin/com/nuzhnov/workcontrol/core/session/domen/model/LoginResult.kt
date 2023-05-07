package com.nuzhnov.workcontrol.core.session.domen.model

sealed interface LoginResult {
    data class Success(val userData: UserData) : LoginResult

    sealed interface Failure : LoginResult {
        object WrongLogin : LoginResult
        object WrongPassword : LoginResult
        object TooManyRequests : LoginResult
        object ServiceError : LoginResult
        object NetworkError : LoginResult
        object UnknownError : LoginResult
    }
}
