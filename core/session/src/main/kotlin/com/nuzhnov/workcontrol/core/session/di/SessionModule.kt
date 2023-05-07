package com.nuzhnov.workcontrol.core.session.di

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.data.repository.SessionRepositoryImpl
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface SessionModule {
    @[Binds Singleton]
    fun bindSessionRepository(instance: SessionRepositoryImpl): SessionRepository
}
