package com.nuzhnov.workcontrol.shared.visitcontrol.di

import com.nuzhnov.workcontrol.shared.visitcontrol.data.repository.ServiceRepository
import com.nuzhnov.workcontrol.shared.visitcontrol.data.repository.WiFiDirectServiceRepository
import com.nuzhnov.workcontrol.shared.visitcontrol.di.annotations.ControlServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitcontrol.di.annotations.IODispatcher
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

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO
}
