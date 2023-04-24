package com.nuzhnov.workcontrol.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faculty")
data class FacultyEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String
)