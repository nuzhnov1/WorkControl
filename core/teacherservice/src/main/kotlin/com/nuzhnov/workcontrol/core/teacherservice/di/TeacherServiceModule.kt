package com.nuzhnov.workcontrol.core.teacherservice.di

import com.nuzhnov.workcontrol.core.teacherservice.di.annotation.TeacherServiceCoroutineScope
import com.nuzhnov.workcontrol.core.teacherservice.presentation.controller.TeacherServiceControllerImpl
import com.nuzhnov.workcontrol.core.teacherservice.domen.controller.TeacherServiceController
import com.nuzhnov.workcontrol.core.teacherservice.domen.repository.TeacherServiceRepository
import com.nuzhnov.workcontrol.core.teacherservice.data.repository.TeacherServiceRepositoryImpl
import com.nuzhnov.workcontrol.core.teacherservice.data.api.ControlServerApi
import com.nuzhnov.workcontrol.core.teacherservice.data.api.ControlServerApiImpl
import com.nuzhnov.workcontrol.core.util.di.annotation.IODispatcher
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServer
import kotlinx.coroutines.*
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface TeacherServiceModule {

    @[Binds Singleton]
    fun provideTeacherServiceController(
        instance: TeacherServiceControllerImpl
    ): TeacherServiceController

    @[Binds Singleton]
    fun provideTeacherServiceRepository(
        instance: TeacherServiceRepositoryImpl
    ): TeacherServiceRepository

    @[Binds Singleton]
    fun provideControlServerApi(
        instance: ControlServerApiImpl
    ): ControlServerApi

    @[Provides Singleton]
    fun provideControlServer(): ControlServer = ControlServer.getDefaultControlServer()

    @[Provides ServiceScoped TeacherServiceCoroutineScope]
    fun provideTeacherServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(context = dispatcher + Job())
}
