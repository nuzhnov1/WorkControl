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
object DatabaseModule {

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
    fun provideStudentGroupDao(appDatabase: AppDatabase): StudentGroupDao =
        appDatabase.getStudentGroupDao()

    @[Provides Singleton]
    fun provideLessonDao(appDatabase: AppDatabase): LessonDao =
        appDatabase.getLessonDao()

    @[Provides Singleton]
    fun provideLessonGroupCrossRefDao(
        appDatabase: AppDatabase
    ): LessonStudentGroupCrossRefDao = appDatabase.getLessonStudentGroupCrossRefDao()

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
