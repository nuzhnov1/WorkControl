package com.nuzhnov.workcontrol.core.visitservice.studentservice.di

import com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.controller.StudentServiceControllerImpl
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.controller.StudentServiceController
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository.StudentServiceRepository
import com.nuzhnov.workcontrol.core.visitservice.studentservice.data.repository.StudentServiceRepositoryImpl
import com.nuzhnov.workcontrol.core.visitservice.studentservice.data.api.VisitorApi
import com.nuzhnov.workcontrol.core.visitservice.studentservice.data.api.VisitorApiImpl
import kotlinx.coroutines.*
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface StudentServiceModule {

    @[Binds Singleton]
    fun bindStudentServiceController(
        instance: StudentServiceControllerImpl
    ): StudentServiceController

    @[Binds Singleton]
    fun bindStudentServiceRepository(
        instance: StudentServiceRepositoryImpl
    ): StudentServiceRepository

    @[Binds Singleton]
    fun bindVisitorApi(
        instance: VisitorApiImpl
    ): VisitorApi
}
