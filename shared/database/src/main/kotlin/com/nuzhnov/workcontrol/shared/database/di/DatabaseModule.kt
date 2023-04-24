package com.nuzhnov.workcontrol.shared.database.di

import com.nuzhnov.workcontrol.shared.database.dao.*
import com.nuzhnov.workcontrol.shared.database.AppDatabase
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface DatabaseModule {
    @[Provides Singleton]
    fun provideBuildingDao(appDatabase: AppDatabase): BuildingDao =
        appDatabase.getBuildingDao()

    @[Provides Singleton]
    fun provideDisciplineDao(appDatabase: AppDatabase): DisciplineDao =
        appDatabase.getDisciplineDao()

    @[Provides Singleton]
    fun provideFacultyDao(appDatabase: AppDatabase): FacultyDao =
        appDatabase.getFacultyDao()

    @[Provides Singleton]
    fun provideGroupDao(appDatabase: AppDatabase): GroupDao =
        appDatabase.getGroupDao()

    @[Provides Singleton]
    fun provideLessonDao(appDatabase: AppDatabase): LessonDao =
        appDatabase.getLessonDao()

    @[Provides Singleton]
    fun provideLessonGroupCrossRefDao(appDatabase: AppDatabase): LessonGroupCrossRefDao =
        appDatabase.getLessonGroupCrossRefDao()

    @[Provides Singleton]
    fun provideParticipantDao(appDatabase: AppDatabase): ParticipantDao =
        appDatabase.getParticipantDao()

    @[Provides Singleton]
    fun provideRoomDao(appDatabase: AppDatabase): RoomDao =
        appDatabase.getRoomDao()

    @[Provides Singleton]
    fun provideStudentDao(appDatabase: AppDatabase): StudentDao =
        appDatabase.getStudentDao()

    @[Provides Singleton]
    fun provideTeacherDisciplineCrossRefDao(
        appDatabase: AppDatabase
    ): TeacherDisciplineCrossRefDao = appDatabase.getTeacherDisciplineCrossRefDao()

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