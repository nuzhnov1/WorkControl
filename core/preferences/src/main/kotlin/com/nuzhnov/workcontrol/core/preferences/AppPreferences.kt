package com.nuzhnov.workcontrol.core.preferences

import com.nuzhnov.workcontrol.core.preferences.model.SessionPreferences
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
    @ApplicationContext private val context: Context,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val sessionPreferencesAdapter: JsonAdapter<SessionPreferences>
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCES_FILENAME
    )


    suspend fun getSessionPreferences(): SessionPreferences? =
        withContext(context = coroutineDispatcher) {
            val key = stringPreferencesKey(SESSION_PREFERENCES_KEY)
            val jsonString = context.dataStore.data
                .map { preferences -> preferences[key] }
                .firstOrNull()

            jsonString?.run { sessionPreferencesAdapter.fromJson(/* string = */ this) }
        }

    suspend fun setSessionPreferences(value: SessionPreferences): Unit =
        withContext(context = coroutineDispatcher) {
            val key = stringPreferencesKey(SESSION_PREFERENCES_KEY)
            val jsonString = sessionPreferencesAdapter.toJson(value)

            context.dataStore.edit { preferences -> preferences[key] = jsonString }
        }

    suspend fun removeSessionPreferences(): Unit =
        withContext(context = coroutineDispatcher) {
            val key = stringPreferencesKey(SESSION_PREFERENCES_KEY)

            context.dataStore.edit { preferences -> preferences.remove(key) }
        }

    fun getSessionPreferencesSync(): SessionPreferences? =
        runBlocking { getSessionPreferences() }


    private companion object {
        const val PREFERENCES_FILENAME = "preferences.json"
        const val SESSION_PREFERENCES_KEY = "USER_PREFERENCES_KEY"
    }
}
