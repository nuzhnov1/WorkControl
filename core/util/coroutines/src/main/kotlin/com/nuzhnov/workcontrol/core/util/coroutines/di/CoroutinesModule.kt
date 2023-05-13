package com.nuzhnov.workcontrol.core.util.coroutines.di

import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.ApplicationCoroutineScope
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal object CoroutinesModule {
    @[Provides Singleton ApplicationCoroutineScope]
    fun provideApplicationCoroutineScope(
        @IODispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(context = dispatcher + Job())

    @[Provides Singleton IODispatcher]
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
