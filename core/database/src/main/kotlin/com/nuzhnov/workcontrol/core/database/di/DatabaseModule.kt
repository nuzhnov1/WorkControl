package com.nuzhnov.workcontrol.core.database.di

import com.nuzhnov.workcontrol.core.database.dao.*
import com.nuzhnov.workcontrol.core.database.AppDatabase
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
internal object DatabaseModule {
    @[Provides Singleton]
    fun provideBuildingDAO(appDatabase: AppDatabase): BuildingDAO =
        appDatabase.getBuildingDAO()

    @[Provides Singleton]
    fun provideRoomDAO(appDatabase: AppDatabase): RoomDAO =
        appDatabase.getRoomDAO()

    @[Provides Singleton]
    fun provideDisciplineDAO(appDatabase: AppDatabase): DisciplineDAO =
        appDatabase.getDisciplineDAO()

    @[Provides Singleton]
    fun provideFacultyDAO(appDatabase: AppDatabase): FacultyDAO =
        appDatabase.getFacultyDAO()

    @[Provides Singleton]
    fun provideGroupDAO(appDatabase: AppDatabase): GroupDAO =
        appDatabase.getGroupDAO()

    @[Provides Singleton]
    fun provideStudentDAO(appDatabase: AppDatabase): StudentDAO =
        appDatabase.getStudentDAO()

    @[Provides Singleton]
    fun provideTeacherDAO(appDatabase: AppDatabase): TeacherDAO =
        appDatabase.getTeacherDAO()

    @[Provides Singleton]
    fun provideTeacherDisciplineCrossRefDAO(appDatabase: AppDatabase): TeacherDisciplineCrossRefDAO =
        appDatabase.getTeacherDisciplineCrossRefDAO()

    @[Provides Singleton]
    fun provideLessonDAO(appDatabase: AppDatabase): LessonDAO =
        appDatabase.getLessonDAO()

    @[Provides Singleton]
    fun provideLessonGroupCrossRefDAO(appDatabase: AppDatabase): LessonGroupCrossRefDAO =
        appDatabase.getLessonGroupCrossRefDAO()

    @[Provides Singleton]
    fun provideParticipantDAO(appDatabase: AppDatabase): ParticipantDAO =
        appDatabase.getParticipantDAO()

    @[Provides Singleton]
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase = Room
        .databaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java,
            name = AppDatabase.APP_DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
}
