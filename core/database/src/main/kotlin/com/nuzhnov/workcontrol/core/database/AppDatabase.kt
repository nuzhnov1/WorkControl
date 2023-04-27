package com.nuzhnov.workcontrol.core.database

import com.nuzhnov.workcontrol.core.database.entity.*
import com.nuzhnov.workcontrol.core.database.dao.*
import androidx.room.RoomDatabase
import androidx.room.Database

@Database(
    entities = [
        BuildingEntity::class,
        DisciplineEntity::class,
        FacultyEntity::class,
        StudentGroupEntity::class,
        LessonEntity::class,
        LessonGroupCrossRefEntity::class,
        ParticipantEntity::class,
        RoomEntity::class,
        StudentEntity::class,
        TeacherDisciplineCrossRefEntity::class,
        TeacherEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBuildingDao(): BuildingDao
    abstract fun getDisciplineDao(): DisciplineDao
    abstract fun getFacultyDao(): FacultyDao
    abstract fun getStudentGroupDao(): StudentGroupDao
    abstract fun getLessonDao(): LessonDao
    abstract fun getLessonStudentGroupCrossRefDao(): LessonStudentGroupCrossRefDao
    abstract fun getParticipantDao(): ParticipantDao
    abstract fun getRoomDao(): RoomDao
    abstract fun getStudentDao(): StudentDao
    abstract fun getTeacherDisciplineCrossRefDao(): TeacherDisciplineCrossRefDao


    companion object {
        const val APP_DATABASE_NAME = "workcontrol.db"
    }
}
