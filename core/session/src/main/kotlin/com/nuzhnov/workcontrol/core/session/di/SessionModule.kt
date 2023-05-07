package com.nuzhnov.workcontrol.core.session.di

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.data.repository.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
internal interface SessionModule {
    @[Provides Singleton]
    fun provideSessionRepository(instance: SessionRepositoryImpl): SessionRepository
}
