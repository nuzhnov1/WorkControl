package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.api.constant.Headers
import com.nuzhnov.workcontrol.core.api.util.Base64Encoder
import retrofit2.http.POST
import retrofit2.http.Header

interface AuthorizationService {
    fun authorize(login: String, password: String): SessionDTO = authorize(
        authData = Base64Encoder.encodeAuthData(login, password)
    )

    @POST("/auth")
    fun authorize(@Header(Headers.X_AUTHENTICATION) authData: String): SessionDTO
}
