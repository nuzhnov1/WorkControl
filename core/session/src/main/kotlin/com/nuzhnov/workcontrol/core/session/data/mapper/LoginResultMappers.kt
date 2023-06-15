package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.data.api.constant.LoginErrorMessages
import com.nuzhnov.workcontrol.core.data.api.util.Response


internal fun Response.Failure.toFailureLoginResult() = when (this) {
    is Response.Failure.HttpClientError.BadRequest -> this.toFailureLoginResult()
    is Response.Failure.HttpClientError.TooManyRequests -> LoginResult.Failure.TooManyRequests(cause)
    is Response.Failure.HttpServerError -> LoginResult.Failure.ServiceError(cause)
    is Response.Failure.NetworkError -> LoginResult.Failure.NetworkError(cause)
    else -> LoginResult.Failure.UnknownError(cause)
}

private fun Response.Failure.HttpClientError.BadRequest.toFailureLoginResult() =
    when (cause.message()) {
        LoginErrorMessages.WRONG_LOGIN -> LoginResult.Failure.WrongLogin(cause)
        LoginErrorMessages.WRONG_PASSWORD -> LoginResult.Failure.WrongPassword(cause)
        else -> LoginResult.Failure.UnknownError(cause)
    }

internal fun Result<LoginResult>.unwrap() = fold(
    onSuccess = { it },
    onFailure = { cause -> LoginResult.Failure.UnknownError(cause) }
)
