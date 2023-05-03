package com.nuzhnov.workcontrol.core.database

import com.nuzhnov.workcontrol.core.database.entity.*
import com.nuzhnov.workcontrol.core.database.dao.*
import androidx.room.RoomDatabase
import androidx.room.Database

@Database(
    entities = [
        BuildingEntity::class,
        RoomEntity::class,
        DisciplineEntity::class,
        FacultyEntity::class,
        GroupEntity::class,
        StudentEntity::class,
        TeacherEntity::class,
        TeacherDisciplineCrossRefEntity::class,
        LessonEntity::class,
        LessonGroupCrossRefEntity::class,
        ParticipantEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBuildingDao(): BuildingDao
    abstract fun getRoomDao(): RoomDao
    abstract fun getDisciplineDao(): DisciplineDao
    abstract fun getFacultyDao(): FacultyDao
    abstract fun getGroupDao(): GroupDao
    abstract fun getStudentDao(): StudentDao
    abstract fun getTeacherDao(): TeacherDao
    abstract fun getTeacherDisciplineCrossRefDao(): TeacherDisciplineCrossRefDao
    abstract fun getLessonDao(): LessonDao
    abstract fun getLessonGroupCrossRefDao(): LessonGroupCrossRefDao
    abstract fun getParticipantDao(): ParticipantDao


    internal companion object {
        const val APP_DATABASE_NAME = "workcontrol.db"
    }
}
