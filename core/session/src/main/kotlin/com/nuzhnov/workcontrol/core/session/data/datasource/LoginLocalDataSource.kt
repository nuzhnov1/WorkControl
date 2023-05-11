package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class LoginLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    fun getSavedLoginFlow(): Flow<String?> = appPreferences
        .getLoginFlow()
        .flowOn(coroutineDispatcher)

    suspend fun saveLogin(login: String): Result<Unit> =
        safeExecute { appPreferences.setLogin(login) }

    suspend fun removeLogin(): Result<Unit> =
        safeExecute { appPreferences.removeLogin() }
}
