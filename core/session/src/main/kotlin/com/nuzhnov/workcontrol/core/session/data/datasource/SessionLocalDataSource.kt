package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.preferences.model.Session
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SessionLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getSavedLogin(): String? =
        withContext(context = coroutineDispatcher) {
            appPreferences.getSession()?.login
        }

    suspend fun saveSession(session: Session): Unit =
        withContext(context = coroutineDispatcher) {
            appPreferences.setSession(session)
        }

    suspend fun removeSession(): Unit =
        withContext(context = coroutineDispatcher) {
            appPreferences.removeSession()
        }
}
