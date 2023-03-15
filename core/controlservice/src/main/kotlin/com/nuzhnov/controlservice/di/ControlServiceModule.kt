package com.nuzhnov.controlservice.di

import com.nuzhnov.controlservice.data.ControlServiceRepository
import com.nuzhnov.controlservice.data.WiFiDirectControlServiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ControlServiceModule {

    @Binds
    @Singleton
    fun bindWiFiDirectControlServiceRepository(
        wiFiDirectControlServiceRepository: WiFiDirectControlServiceRepository
    ): ControlServiceRepository
}
