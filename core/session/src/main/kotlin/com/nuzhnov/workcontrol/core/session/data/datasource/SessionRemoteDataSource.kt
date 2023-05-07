package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.api.service.AuthorizationService
import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SessionRemoteDataSource @Inject constructor(
    private val authorizationService: AuthorizationService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun login(login: String, password: String): Response<SessionDTO> =
        withContext(context = coroutineDispatcher) {
            safeApiCall { authorizationService.login(login, password) }
        }
}
