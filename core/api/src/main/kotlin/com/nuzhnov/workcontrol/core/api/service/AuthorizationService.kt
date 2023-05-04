package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.api.constant.Headers
import com.nuzhnov.workcontrol.core.api.util.Base64Encoder
import com.nuzhnov.workcontrol.core.api.annotation.PermittedToNotAuthorized
import retrofit2.http.POST
import retrofit2.http.Header

interface AuthorizationService {
    @PermittedToNotAuthorized
    suspend fun login(login: String, password: String): SessionDTO = login(
        authData = Base64Encoder.encodeAuthData(login, password)
    )

    @[POST("/auth") PermittedToNotAuthorized]
    suspend fun login(@Header(Headers.X_AUTHENTICATION) authData: String): SessionDTO
}
