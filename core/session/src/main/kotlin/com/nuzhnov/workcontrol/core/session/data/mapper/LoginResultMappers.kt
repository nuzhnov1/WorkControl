package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.api.constant.LoginErrorMessages
import com.nuzhnov.workcontrol.core.api.util.Response


internal fun Response<UserData>.toLoginResult(): LoginResult = when (this) {
    is Response.Success<UserData> -> LoginResult.Success(userData = value)
    is Response.Failure -> this.toFailureLoginResult()
}

internal fun Response.Failure.toFailureLoginResult(): LoginResult.Failure = when (this) {
    is Response.Failure.HttpClientError.BadRequest -> this.toFailureLoginResult()
    is Response.Failure.HttpClientError.TooManyRequests -> LoginResult.Failure.TooManyRequests
    is Response.Failure.HttpServerError -> LoginResult.Failure.ServiceError
    is Response.Failure.NetworkError -> LoginResult.Failure.NetworkError
    else -> LoginResult.Failure.UnknownError
}

private fun Response.Failure.HttpClientError.BadRequest.toFailureLoginResult(): LoginResult.Failure =
    when (message) {
        LoginErrorMessages.WRONG_LOGIN -> LoginResult.Failure.WrongLogin
        LoginErrorMessages.WRONG_PASSWORD -> LoginResult.Failure.WrongPassword
        else -> LoginResult.Failure.UnknownError
    }
