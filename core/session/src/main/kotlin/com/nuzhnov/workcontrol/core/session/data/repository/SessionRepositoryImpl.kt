package com.nuzhnov.workcontrol.core.session.data.repository

import com.nuzhnov.workcontrol.core.session.data.datasource.*
import com.nuzhnov.workcontrol.core.session.data.mapper.toFailureLoginResult
import com.nuzhnov.workcontrol.core.session.data.mapper.toSession
import com.nuzhnov.workcontrol.core.session.data.mapper.unwrap
import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.mapper.toLoadStatus
import com.nuzhnov.workcontrol.core.mapper.unwrap
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SessionRepositoryImpl @Inject constructor(
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val loginLocalDataSource: LoginLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : SessionRepository {

    override suspend fun login(
        login: String,
        password: String,
        isLoginSave: Boolean
    ): LoginResult = safeExecute {
        when (val authorizationResponse = sessionRemoteDataSource.login(login, password)) {
            is Response.Success<SessionDTO> -> {
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
        safeExecute {
            userLocalDataSource.removeUserData().getOrThrow()
            sessionLocalDataSource.removeSession().getOrThrow()
            loginLocalDataSource.removeLogin().getOrThrow()
        }
    }
    .unwrap()

    override suspend fun logout(): Result<Unit> = safeExecute {
        userLocalDataSource.removeUserData().getOrThrow()
        sessionLocalDataSource.removeSession().getOrThrow()
    }

    override fun getUserDataFlow(): Flow<UserData?> = userLocalDataSource.getUserDataFlow()

    override fun getSavedLoginFlow(): Flow<String?> = loginLocalDataSource.getSavedLoginFlow()

    override suspend fun loadUserData(): LoadResult<UserData> = safeExecute {
        val session = sessionLocalDataSource
            .getSession()
            .getOrThrow() ?: throw IllegalStateException("unauthorized")

        val sessionRole = session.role
        val getUserDataResponse = userRemoteDataSource.getUserData(sessionRole)

        if (getUserDataResponse is Response.Success<UserData>) {
            userLocalDataSource
                .saveUserData(userData = getUserDataResponse.value)
                .getOrThrow()
        }

        getUserDataResponse.toLoadStatus()
    }.unwrap()
}
