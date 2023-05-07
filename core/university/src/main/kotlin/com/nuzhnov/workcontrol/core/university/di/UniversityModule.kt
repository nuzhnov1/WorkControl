package com.nuzhnov.workcontrol.core.university.di

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.data.repository.UniversityRepositoryImpl
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface UniversityModule {
    @[Binds Singleton]
    fun bindUniversityRepository(instance: UniversityRepositoryImpl): UniversityRepository
}
