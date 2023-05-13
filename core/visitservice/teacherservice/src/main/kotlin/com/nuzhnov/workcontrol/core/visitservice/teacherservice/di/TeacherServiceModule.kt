package com.nuzhnov.workcontrol.core.visitservice.teacherservice.di

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.controller.TeacherServiceControllerImpl
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.controller.TeacherServiceController
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.repository.TeacherServiceRepository
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.repository.TeacherServiceRepositoryImpl
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.api.ControlServerApi
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.api.ControlServerApiImpl
import kotlinx.coroutines.*
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface TeacherServiceModule {
    @[Binds Singleton]
    fun bindTeacherServiceController(
        instance: TeacherServiceControllerImpl
    ): TeacherServiceController

    @[Binds Singleton]
    fun bindTeacherServiceRepository(
        instance: TeacherServiceRepositoryImpl
    ): TeacherServiceRepository

    @[Binds Singleton]
    fun bindControlServerApi(
        instance: ControlServerApiImpl
    ): ControlServerApi
}
