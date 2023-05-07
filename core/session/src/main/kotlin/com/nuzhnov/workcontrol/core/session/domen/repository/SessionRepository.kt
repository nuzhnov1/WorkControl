package com.nuzhnov.workcontrol.core.session.domen.repository

import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.model.UserData

interface SessionRepository {
    suspend fun login(login: String, password: String): LoginResult
    suspend fun logout(): Result<Unit>
    suspend fun getUserData(): Result<UserData?>
    suspend fun getPersistedLogin(): Result<String?>
}
