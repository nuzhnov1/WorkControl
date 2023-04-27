package com.nuzhnov.workcontrol.core.visitservice.teacherservice.di

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.di.annotation.TeacherServiceCoroutineScope
import com.nuzhnov.workcontrol.core.visitservice.util.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal object CoroutinesModule {

    @[Provides ServiceScoped TeacherServiceCoroutineScope]
    fun provideTeacherServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(context = dispatcher + Job())
}
