package com.nuzhnov.workcontrol.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teacher")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val email: String
)
