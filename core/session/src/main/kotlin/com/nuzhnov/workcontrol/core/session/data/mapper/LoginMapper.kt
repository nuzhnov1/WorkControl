package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.api.constant.LoginErrorMessages
import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.preferences.model.Session


internal fun SessionDTO.toSession(login: String): Session = Session(
    id = id,
    login = login,
    authorizationToken = authorizationToken,
    role = role
)

internal fun Response<UserData>.toLoginResult(): LoginResult = when (this) {
    is Response.Success<UserData> -> LoginResult.Success(userData = value)
    is Response.Failure -> this.toLoginResult()
}

internal fun Response.Failure.toLoginResult(): LoginResult = when (this) {
    is Response.Failure.HttpClientError.BadRequest -> this.toLoginResult()
    is Response.Failure.HttpClientError.TooManyRequests -> LoginResult.Failure.TooManyRequests
    is Response.Failure.HttpServerError -> LoginResult.Failure.ServiceError
    is Response.Failure.NetworkError -> LoginResult.Failure.NetworkError
    else -> LoginResult.Failure.UnknownError
}

private fun Response.Failure.HttpClientError.BadRequest.toLoginResult(): LoginResult =
    when (message) {
        LoginErrorMessages.WRONG_LOGIN -> LoginResult.Failure.WrongLogin
        LoginErrorMessages.WRONG_PASSWORD -> LoginResult.Failure.WrongPassword
        else -> LoginResult.Failure.UnknownError
    }
