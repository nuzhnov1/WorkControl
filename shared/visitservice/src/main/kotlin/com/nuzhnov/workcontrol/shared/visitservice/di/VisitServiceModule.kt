package com.nuzhnov.workcontrol.shared.visitservice.di

import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.ControlServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitorServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitorApi
import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitorApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.api.ControlServerApi
import com.nuzhnov.workcontrol.shared.visitservice.data.api.ControlServerApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.repository.VisitorServiceRepositoryImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.repository.ControlServiceRepositoryImpl
import com.nuzhnov.workcontrol.core.visitcontrol.visitor.Visitor
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServer
import kotlinx.coroutines.*
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
    fun provideControlServiceRepository(
        instance: ControlServiceRepositoryImpl
    ): ControlServiceRepository

    @Binds
    @Singleton
    fun provideVisitorServiceRepository(
        instance: VisitorServiceRepositoryImpl
    ): VisitorServiceRepository

    @Binds
    @Singleton
    fun provideControlServerApi(
        instance: ControlServerApiImpl
    ): ControlServerApi

    @Binds
    @Singleton
    fun provideVisitorApi(
        instance: VisitorApiImpl
    ): VisitorApi

    @Provides
    @Singleton
    fun provideControlServer() = ControlServer.getDefaultControlServer()

    @Provides
    @Singleton
    fun provideVisitor() = Visitor.getDefaultVisitor()

    @Provides
    @ServiceScoped
    @ControlServiceCoroutineScope
    fun provideControlServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = CoroutineScope(context = dispatcher + Job())

    @Provides
    @ServiceScoped
    @VisitorServiceCoroutineScope
    fun provideVisitorServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = CoroutineScope(context = dispatcher + Job())

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO
}
