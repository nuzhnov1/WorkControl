package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.preferences.model.Session
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SessionLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getSession(): Result<Session?> =
        safeExecute(context = coroutineDispatcher) { appPreferences.getSession() }

    suspend fun saveSession(session: Session): Result<Unit> =
        safeExecute(context = coroutineDispatcher) { appPreferences.setSession(session) }

    suspend fun removeSession(): Result<Unit> =
        safeExecute(context = coroutineDispatcher) { appPreferences.removeSession() }
}
