package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LoginLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences
) {

    fun getSavedLoginFlow(): Flow<String?> =
        appPreferences.getLoginFlow()

    suspend fun saveLogin(login: String): Result<Unit> =
        safeExecute { appPreferences.setLogin(login) }

    suspend fun removeLogin(): Result<Unit> =
        safeExecute { appPreferences.removeLogin() }
}
