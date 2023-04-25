package com.nuzhnov.workcontrol.shared.util.di

import com.nuzhnov.workcontrol.shared.util.di.annotation.ApplicationCoroutineScope
import com.nuzhnov.workcontrol.shared.util.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface CoroutinesModule {
    @[Provides Singleton ApplicationCoroutineScope]
    fun provideApplicationCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(context = dispatcher + Job())

    @[Provides Singleton IODispatcher]
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}