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
    abstract fun getBuildingDAO(): BuildingDAO
    abstract fun getRoomDAO(): RoomDAO
    abstract fun getDisciplineDAO(): DisciplineDAO
    abstract fun getFacultyDAO(): FacultyDAO
    abstract fun getGroupDAO(): GroupDAO
    abstract fun getStudentDAO(): StudentDAO
    abstract fun getTeacherDAO(): TeacherDAO
    abstract fun getTeacherDisciplineCrossRefDAO(): TeacherDisciplineCrossRefDAO
    abstract fun getLessonDAO(): LessonDAO
    abstract fun getLessonGroupCrossRefDAO(): LessonGroupCrossRefDAO
    abstract fun getParticipantDAO(): ParticipantDAO


    internal companion object {
        const val APP_DATABASE_NAME = "workcontrol.db"
    }
}
