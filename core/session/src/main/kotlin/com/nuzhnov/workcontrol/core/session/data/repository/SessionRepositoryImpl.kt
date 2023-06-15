package com.nuzhnov.workcontrol.core.session.data.repository

import com.nuzhnov.workcontrol.core.session.data.datasource.*
import com.nuzhnov.workcontrol.core.session.data.mapper.toFailureLoginResult
import com.nuzhnov.workcontrol.core.session.data.mapper.toSession
import com.nuzhnov.workcontrol.core.session.data.mapper.unwrap
import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.exception.SessionException
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.mapper.toLoadResult
import com.nuzhnov.workcontrol.core.data.mapper.toOperationResult
import com.nuzhnov.workcontrol.core.data.mapper.unwrap
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SessionRepositoryImpl @Inject constructor(
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val loginLocalDataSource: LoginLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : SessionRepository {

    override suspend fun login(login: String, password: String, isLoginSave: Boolean) =
        safeExecute(context = coroutineDispatcher) {
            when (val authorizationResponse = sessionRemoteDataSource.login(login, password)) {
                is Response.Success -> {
                    val session = authorizationResponse.value.toSession()

                    sessionLocalDataSource.saveSession(session)

                    if (isLoginSave) {
                        loginLocalDataSource.saveLogin(login)
                    }

                    when (refreshUserData()) {
                        is LoadResult.Success -> LoginResult.Success
                        else -> throw SessionException("failed to load the user data")
                    }
                }

                is Response.Failure -> authorizationResponse.toFailureLoginResult()
            }
        }.onFailure {
            safeExecute(context = coroutineDispatcher) {
                userLocalDataSource.removeUserData()
                sessionLocalDataSource.removeSession()
                loginLocalDataSource.removeLogin()
            }
        }.unwrap()

    override suspend fun logout() = safeExecute(context = coroutineDispatcher) {
        userLocalDataSource.removeUserData()
        sessionLocalDataSource.removeSession()
    }.toOperationResult()

    override fun getUserDataFlow() = userLocalDataSource
        .getUserDataFlow()
        .flowOn(context = coroutineDispatcher)

    override fun getSavedLoginFlow() = loginLocalDataSource
        .getSavedLoginFlow()
        .flowOn(context = coroutineDispatcher)

    override suspend fun refreshUserData() = withContext(context = coroutineDispatcher) {
        val session = sessionLocalDataSource.getSession() ?: throw SessionException("unauthorized")

        safeExecute {
            val response = userRemoteDataSource.getUserData(session.role)

            if (response is Response.Success) {
                userLocalDataSource.saveUserData(userData = response.value)
            }

            response.toLoadResult()
        }.unwrap()
    }
}
