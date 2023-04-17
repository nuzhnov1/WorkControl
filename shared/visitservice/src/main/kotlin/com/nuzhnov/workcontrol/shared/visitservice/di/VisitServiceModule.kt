package com.nuzhnov.workcontrol.shared.visitservice.di

import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.ControlServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitorServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.data.repository.ControlServiceRepositoryImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.repository.VisitorServiceRepositoryImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.local.VisitorsDatabase
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.ControlServerApi
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.ControlServerApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.VisitorApi
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.VisitorApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.VISITORS_DATABASE_NAME
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServer
import com.nuzhnov.workcontrol.core.visitcontrol.visitor.Visitor
import kotlinx.coroutines.*
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface VisitServiceModule {

    @[Binds Singleton]
    fun provideControlServiceRepository(
        instance: ControlServiceRepositoryImpl
    ): ControlServiceRepository

    @[Binds Singleton]
    fun provideVisitorServiceRepository(
        instance: VisitorServiceRepositoryImpl
    ): VisitorServiceRepository

    @[Binds Singleton]
    fun provideControlServerApi(instance: ControlServerApiImpl): ControlServerApi

    @[Binds Singleton]
    fun provideVisitorApi(instance: VisitorApiImpl): VisitorApi

    @[Provides Singleton]
    fun provideControlServer() = ControlServer.getDefaultControlServer()

    @[Provides Singleton]
    fun provideVisitor() = Visitor.getDefaultVisitor()

    @[Provides Singleton]
    fun provideVisitorsDao(database: VisitorsDatabase) = database.provideVisitorsDao()

    @[Provides Singleton]
    fun provideVisitorsDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, VisitorsDatabase::class.java, name = VISITORS_DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @[Provides ServiceScoped ControlServiceCoroutineScope]
    fun provideControlServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = CoroutineScope(context = dispatcher + Job())

    @[Provides ServiceScoped VisitorServiceCoroutineScope]
    fun provideVisitorServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = CoroutineScope(context = dispatcher + Job())

    @[Provides Singleton IODispatcher]
    fun provideIODispatcher() = Dispatchers.IO
}
