package com.nuzhnov.workcontrol.core.lesson.di

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.lesson.data.repository.LessonRepositoryImpl
import com.nuzhnov.workcontrol.core.lesson.data.repository.ParticipantRepositoryImpl
import javax.inject.Singleton
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal interface LessonModule {
    @[Binds Singleton]
    fun bindLessonRepository(instance: LessonRepositoryImpl): LessonRepository

    @[Binds Singleton]
    fun bindParticipantRepository(instance: ParticipantRepositoryImpl): ParticipantRepository
}
