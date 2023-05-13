package com.nuzhnov.workcontrol.core.visitservice.studentservice.di

import com.nuzhnov.workcontrol.core.visitservice.studentservice.di.annotation.StudentServiceCoroutineScope
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
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
    @[Provides ServiceScoped StudentServiceCoroutineScope]
    fun provideStudentServiceCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(context = dispatcher + Job())
}
