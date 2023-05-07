package com.nuzhnov.workcontrol.core.session.data.repository

import com.nuzhnov.workcontrol.core.session.data.datasource.SessionRemoteDataSource
import com.nuzhnov.workcontrol.core.session.data.datasource.SessionLocalDataSource
import com.nuzhnov.workcontrol.core.session.data.datasource.UserRemoteDataSource
import com.nuzhnov.workcontrol.core.session.data.datasource.UserLocalDataSource
import com.nuzhnov.workcontrol.core.session.data.mapper.toLoginResult
import com.nuzhnov.workcontrol.core.session.data.mapper.toFailureLoginResult
import com.nuzhnov.workcontrol.core.session.data.mapper.toSession
import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.api.util.Response
import javax.inject.Inject

internal class SessionRepositoryImpl @Inject constructor(
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : SessionRepository {

    override suspend fun login(login: String, password: String): LoginResult =
        runCatching {
            when (val authorizationResponse = sessionRemoteDataSource.login(login, password)) {
                is Response.Success<SessionDTO> -> {
                    val session = authorizationResponse.value.toSession(login)
                    val sessionRole = session.role

                    sessionLocalDataSource.saveSession(session)

                    val getUserDataResponse = userRemoteDataSource.getUserData(sessionRole)

                    if (getUserDataResponse is Response.Success<UserData>) {
                        userLocalDataSource.saveUserData(userData = getUserDataResponse.value)
                    }

                    getUserDataResponse.toLoginResult()
                }

                is Response.Failure -> authorizationResponse.toFailureLoginResult()
            }
        }
        .onFailure { logout() }
        .fold(onSuccess = { it }, onFailure = { LoginResult.Failure.UnknownError })

    override suspend fun logout(): Result<Unit> =
        runCatching {
            userLocalDataSource.removeUserData()
            sessionLocalDataSource.removeSession()
        }

    override suspend fun getUserData(): Result<UserData?> =
        runCatching { userLocalDataSource.getUserData() }

    override suspend fun getPersistedLogin(): Result<String?> =
        runCatching { sessionLocalDataSource.getSavedLogin() }
}
