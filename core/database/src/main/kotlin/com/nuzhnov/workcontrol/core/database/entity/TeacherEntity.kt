package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "teacher")
data class TeacherEntity(
    @[PrimaryKey(autoGenerate = false) ColumnInfo(name = "teacher_id")]
    val teacherID: Long,
    val name: String,
    val email: String
)
