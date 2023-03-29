package com.nuzhnov.workcontrol.shared.visitservice.di

import com.nuzhnov.workcontrol.core.visitcontrol.client.Client
import com.nuzhnov.workcontrol.core.visitcontrol.server.Server
import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlClientApi
import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlClientApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlServerApi
import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlServerApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.repository.VisitClientServiceRepositoryImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.repository.VisitControlServiceRepositoryImpl
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitClientServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitControlServiceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface VisitServiceModule {

    @Binds
    @Singleton
    fun provideVisitControlServiceRepository(
        instance: VisitControlServiceRepositoryImpl
    ): VisitControlServiceRepository

    @Binds
    @Singleton
    fun provideVisitClientServiceRepository(
        instance: VisitClientServiceRepositoryImpl
    ): VisitClientServiceRepository

    @Binds
    @Singleton
    fun provideVisitControlServiceApi(
        instance: VisitControlServerApiImpl
    ): VisitControlServerApi

    @Binds
    @Singleton
    fun provideVisitControlClientApi(
        instance: VisitControlClientApiImpl
    ): VisitControlClientApi

    @Provides
    @Singleton
    fun provideVisitControlServer() = Server.getDefaultServer()

    @Provides
    @Singleton
    fun provideVisitControlClient() = Client.getDefaultClient()

    @Provides
    @ServiceScoped
    @VisitServiceCoroutineScope
    fun provideVisitServiceCoroutineScope() = CoroutineScope(context = Dispatchers.IO + Job())

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO
}
