package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.preferences.model.Session
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SessionLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences
) {

    suspend fun getSession(): Result<Session?> =
        safeExecute { appPreferences.getSession() }

    suspend fun saveSession(session: Session): Result<Unit> =
        safeExecute { appPreferences.setSession(session) }

    suspend fun removeSession(): Result<Unit> =
        safeExecute { appPreferences.removeSession() }
}
