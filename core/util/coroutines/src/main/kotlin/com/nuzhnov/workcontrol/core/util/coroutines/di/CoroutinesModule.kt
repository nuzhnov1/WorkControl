package com.nuzhnov.workcontrol.core.util.coroutines.di

import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal object CoroutinesModule {
    @[Provides Singleton IODispatcher]
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
