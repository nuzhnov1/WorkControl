package com.nuzhnov.workcontrol.core.visitservice.studentservice.di

import com.nuzhnov.workcontrol.common.visitcontrol.visitor.Visitor
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal object VisitorControlApiModule {
    @[Provides Singleton]
    fun provideVisitor() = Visitor.getDefaultVisitor()
}
