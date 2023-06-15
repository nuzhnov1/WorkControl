package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import javax.inject.Inject

internal class LoginLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences
) {

    fun getSavedLoginFlow() = appPreferences.getLoginFlow()

    suspend fun saveLogin(login: String) = appPreferences.setLogin(login)

    suspend fun removeLogin() = appPreferences.removeLogin()
}
