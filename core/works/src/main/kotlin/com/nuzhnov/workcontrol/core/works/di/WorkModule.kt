package com.nuzhnov.workcontrol.core.works.di

import com.nuzhnov.workcontrol.core.works.domain.repository.WorkRepository
import com.nuzhnov.workcontrol.core.works.data.repository.WorkRepositoryImpl
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface WorkModule {
    @[Binds Singleton]
    fun bindWorkRepository(instance: WorkRepositoryImpl): WorkRepository
}
