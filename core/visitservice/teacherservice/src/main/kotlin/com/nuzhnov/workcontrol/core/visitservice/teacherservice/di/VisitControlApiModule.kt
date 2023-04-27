package com.nuzhnov.workcontrol.core.visitservice.teacherservice.di

import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServer
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal object VisitControlApiModule {

    @[Provides Singleton]
    fun provideControlServer(): ControlServer = ControlServer.getDefaultControlServer()
}
