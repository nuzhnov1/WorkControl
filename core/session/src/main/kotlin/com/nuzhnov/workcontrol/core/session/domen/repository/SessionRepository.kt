package com.nuzhnov.workcontrol.core.session.domen.repository

import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.models.util.OperationResult
import kotlinx.coroutines.flow.Flow

internal interface SessionRepository {
    suspend fun login(login: String, password: String, isLoginSave: Boolean): LoginResult

    suspend fun logout(): OperationResult

    fun getUserDataFlow(): Flow<UserData?>

    fun getSavedLoginFlow(): Flow<String?>

    suspend fun refreshUserData(): LoadResult
}
