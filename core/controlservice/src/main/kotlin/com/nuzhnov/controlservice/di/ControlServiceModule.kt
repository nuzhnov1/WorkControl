package com.nuzhnov.controlservice.di

import com.nuzhnov.controlservice.data.repository.ServiceRepository
import com.nuzhnov.controlservice.data.repository.WiFiDirectServiceRepository
import com.nuzhnov.controlservice.di.annotations.ControlServiceCoroutineScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ControlServiceModule {

    @Binds
    @Singleton
    fun bindWiFiDirectControlServiceRepository(
        wiFiDirectControlServiceRepository: WiFiDirectServiceRepository
    ): ServiceRepository

    @Provides
    @Singleton
    @ControlServiceCoroutineScope
    fun provideControlServiceCoroutineScope() = CoroutineScope(Dispatchers.IO + Job())
}
