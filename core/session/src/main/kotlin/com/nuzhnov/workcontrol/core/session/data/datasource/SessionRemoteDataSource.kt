package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.AuthorizationService
import com.nuzhnov.workcontrol.core.data.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SessionRemoteDataSource @Inject constructor(
    private val authorizationService: AuthorizationService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun login(login: String, password: String): Response<SessionDTO> =
        safeApiCall(context = coroutineDispatcher) { authorizationService.login(login, password) }
}
