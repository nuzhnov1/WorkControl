package com.nuzhnov.workcontrol.core.preferences

import com.nuzhnov.workcontrol.core.preferences.model.Session
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import com.squareup.moshi.JsonAdapter

class AppPreferences @Inject internal constructor(
    private val sessionAdapter: JsonAdapter<Session>,
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCES_FILENAME
    )


    suspend fun getSession(): Session? = getSessionFlow().firstOrNull()

    fun getSessionSync(): Session? = runBlocking { getSession() }

    fun getSessionFlow(): Flow<Session?> = context.dataStore
        .data
        .map { preferences -> preferences[sessionPreferencesKey] }
        .map { jsonString -> jsonString?.run { sessionAdapter.fromJson(/* string = */ this) } }

    suspend fun setSession(value: Session) {
        context.dataStore.edit { preferences ->
            preferences[sessionPreferencesKey] = sessionAdapter.toJson(value)
        }
    }

    suspend fun removeSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(sessionPreferencesKey)
        }
    }

    fun getLoginFlow(): Flow<String?> = context.dataStore
        .data
        .map { preferences -> preferences[sessionPreferencesKey] }

    suspend fun setLogin(value: String) {
        context.dataStore.edit { preferences ->
            preferences[loginPreferencesKey] = value
        }
    }

    suspend fun removeLogin() {
        context.dataStore.edit { preferences ->
            preferences.remove(loginPreferencesKey)
        }
    }


    private companion object {
        const val PREFERENCES_FILENAME = "preferences.json"
        const val SESSION_KEY = "SESSION_KEY"
        const val LOGIN_KEY = "LOGIN_KEY"

        val sessionPreferencesKey = stringPreferencesKey(SESSION_KEY)
        val loginPreferencesKey = stringPreferencesKey(LOGIN_KEY)
    }
}
