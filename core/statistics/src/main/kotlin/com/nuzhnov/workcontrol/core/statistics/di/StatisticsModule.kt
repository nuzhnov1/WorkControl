package com.nuzhnov.workcontrol.core.statistics.di

import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import com.nuzhnov.workcontrol.core.statistics.data.repository.StatisticsRepositoryImpl
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface StatisticsModule {
    @[Binds Singleton]
    fun bindStatisticsRepository(instance: StatisticsRepositoryImpl): StatisticsRepository
}
