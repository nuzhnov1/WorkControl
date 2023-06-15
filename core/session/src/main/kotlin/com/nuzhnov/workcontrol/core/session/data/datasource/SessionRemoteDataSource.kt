package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.AuthorizationService
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import javax.inject.Inject

internal class SessionRemoteDataSource @Inject constructor(
    private val authorizationService: AuthorizationService
) {

    suspend fun login(login: String, password: String) = safeApiCall { authorizationService.login(login, password) }
}
