package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.preferences.model.Session
import javax.inject.Inject

internal class SessionLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences
) {

    suspend fun getSession() = appPreferences.getSession()

    suspend fun saveSession(session: Session) = appPreferences.setSession(session)

    suspend fun removeSession() = appPreferences.removeSession()
}
