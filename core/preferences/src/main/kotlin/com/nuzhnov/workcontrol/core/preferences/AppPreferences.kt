package com.nuzhnov.workcontrol.core.preferences

import com.nuzhnov.workcontrol.core.preferences.model.Session
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
    private val sessionAdapter: JsonAdapter<Session>
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCES_FILENAME
    )


    suspend fun getSession(): Session? = withContext(context = coroutineDispatcher) {
        val key = stringPreferencesKey(SESSION_KEY)
        val jsonString = context.dataStore.data
            .map { preferences -> preferences[key] }
            .firstOrNull()

        jsonString?.run { sessionAdapter.fromJson(/* string = */ this) }
    }

    suspend fun setSession(value: Session): Unit = withContext(context = coroutineDispatcher) {
        val key = stringPreferencesKey(SESSION_KEY)
        val jsonString = sessionAdapter.toJson(value)

        context.dataStore.edit { preferences -> preferences[key] = jsonString }
    }

    suspend fun removeSession(): Unit = withContext(context = coroutineDispatcher) {
        val key = stringPreferencesKey(SESSION_KEY)

        context.dataStore.edit { preferences -> preferences.remove(key) }
    }

    fun getSessionSync(): Session? = runBlocking { getSession() }


    private companion object {
        const val PREFERENCES_FILENAME = "preferences.json"
        const val SESSION_KEY = "SESSION_KEY"
    }
}
