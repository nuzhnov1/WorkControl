package com.nuzhnov.workcontrol.core.preferences.di

import com.nuzhnov.workcontrol.core.preferences.model.Session
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
    fun provideSessionAdapter(moshi: Moshi): JsonAdapter<Session> =
        moshi.adapter(Session::class.java)

    @[Provides Singleton]
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}
