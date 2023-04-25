package com.nuzhnov.workcontrol.core.database

import com.nuzhnov.workcontrol.core.database.entity.*
import com.nuzhnov.workcontrol.core.database.dao.*
import com.nuzhnov.workcontrol.core.database.converter.Converters
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters

@Database(
    entities = [
        BuildingEntity::class,
        DisciplineEntity::class,
        FacultyEntity::class,
        GroupEntity::class,
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
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBuildingDao(): BuildingDao
    abstract fun getDisciplineDao(): DisciplineDao
    abstract fun getFacultyDao(): FacultyDao
    abstract fun getGroupDao(): GroupDao
    abstract fun getLessonDao(): LessonDao
    abstract fun getLessonGroupCrossRefDao(): LessonGroupCrossRefDao
    abstract fun getParticipantDao(): ParticipantDao
    abstract fun getRoomDao(): RoomDao
    abstract fun getStudentDao(): StudentDao
    abstract fun getTeacherDisciplineCrossRefDao(): TeacherDisciplineCrossRefDao


    companion object {
        const val APP_DATABASE_NAME = "workcontrol.db"
    }
}
