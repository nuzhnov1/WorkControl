package com.nuzhnov.workcontrol.core.session.data.repository

import com.nuzhnov.workcontrol.core.session.data.datasource.*
import com.nuzhnov.workcontrol.core.session.data.mapper.toFailureLoginResult
import com.nuzhnov.workcontrol.core.session.data.mapper.toSession
import com.nuzhnov.workcontrol.core.session.data.mapper.unwrap
import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.mapper.toLoadResult
import com.nuzhnov.workcontrol.core.data.mapper.unwrap
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
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

    override suspend fun login(
        login: String,
        password: String,
        isLoginSave: Boolean
    ): LoginResult = safeExecute(context = coroutineDispatcher) {
        when (val authorizationResponse = sessionRemoteDataSource.login(login, password)) {
            is Response.Success -> {
                val session = authorizationResponse.value.toSession()

                sessionLocalDataSource.saveSession(session).getOrThrow()

                if (isLoginSave) {
                    loginLocalDataSource.saveLogin(login).getOrThrow()
                }

                when (val userDataLoadResult = loadUserData()) {
                    is LoadResult.Success -> LoginResult.Success(userDataLoadResult.data)
                    else -> throw IllegalStateException("failed to load user data")
                }
            }

            is Response.Failure -> authorizationResponse.toFailureLoginResult()
        }
    }
        .onFailure {
            safeExecute(context = coroutineDispatcher) {
                userLocalDataSource.removeUserData().getOrThrow()
                sessionLocalDataSource.removeSession().getOrThrow()
                loginLocalDataSource.removeLogin().getOrThrow()
            }
        }
        .unwrap()

    override suspend fun logout(): Result<Unit> = safeExecute(context = coroutineDispatcher) {
        userLocalDataSource.removeUserData().getOrThrow()
        sessionLocalDataSource.removeSession().getOrThrow()
    }

    override fun getUserDataFlow(): Flow<UserData?> = userLocalDataSource
        .getUserDataFlow()
        .flowOn(context = coroutineDispatcher)

    override fun getSavedLoginFlow(): Flow<String?> = loginLocalDataSource
        .getSavedLoginFlow()
        .flowOn(context = coroutineDispatcher)

    override suspend fun loadUserData(): LoadResult<UserData> =
        safeExecute(context = coroutineDispatcher) {
            val session = sessionLocalDataSource
                .getSession()
                .getOrThrow() ?: throw IllegalStateException("unauthorized")

            val sessionRole = session.role
            val getUserDataResponse = userRemoteDataSource.getUserData(sessionRole)

            if (getUserDataResponse is Response.Success) {
                userLocalDataSource
                    .saveUserData(userData = getUserDataResponse.value)
                    .getOrThrow()
            }

            getUserDataResponse.toLoadResult()
        }.unwrap()
}
