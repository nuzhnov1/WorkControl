package com.nuzhnov.workcontrol.core.studentservice.di

import com.nuzhnov.workcontrol.core.studentservice.di.annotations.StudentServiceCoroutineScope
import com.nuzhnov.workcontrol.core.studentservice.presentation.controller.StudentServiceControllerImpl
import com.nuzhnov.workcontrol.core.studentservice.domen.controller.StudentServiceController
import com.nuzhnov.workcontrol.core.studentservice.domen.repository.StudentServiceRepository
import com.nuzhnov.workcontrol.core.studentservice.data.repository.StudentServiceRepositoryImpl
import com.nuzhnov.workcontrol.core.studentservice.data.api.VisitorApi
import com.nuzhnov.workcontrol.core.studentservice.data.api.VisitorApiImpl
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.Visitor
import com.nuzhnov.workcontrol.core.util.di.annotation.IODispatcher
import kotlinx.coroutines.*
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface StudentServiceModule {

    @[Binds Singleton]
    fun provideStudentServiceController(
        instance: StudentServiceControllerImpl
    ): StudentServiceController

    @[Binds Singleton]
    fun provideStudentServiceRepository(
        instance: StudentServiceRepositoryImpl
    ): StudentServiceRepository

    @[Binds Singleton]
    fun provideVisitorApi(
        instance: VisitorApiImpl
    ): VisitorApi

    @[Provides Singleton]
    fun provideVisitor() = Visitor.getDefaultVisitor()

    @[Provides ServiceScoped StudentServiceCoroutineScope]
    fun provideStudentServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(context = dispatcher + Job())
}
