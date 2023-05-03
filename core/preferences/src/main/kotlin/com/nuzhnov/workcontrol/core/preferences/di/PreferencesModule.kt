package com.nuzhnov.workcontrol.core.preferences.di

import com.nuzhnov.workcontrol.core.preferences.model.SessionPreferences
import com.squareup.moshi.JsonAdapter
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.squareup.moshi.Moshi

@[Module InstallIn(SingletonComponent::class)]
internal object PreferencesModule {
    @[Provides Singleton]
    fun provideSessionPreferencesAdapter(moshi: Moshi): JsonAdapter<SessionPreferences> =
        moshi.adapter(SessionPreferences::class.java)

    @[Provides Singleton]
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}
