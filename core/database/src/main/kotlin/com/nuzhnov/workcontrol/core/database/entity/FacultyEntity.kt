package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "faculty")
data class FacultyEntity(
    @[PrimaryKey(autoGenerate = false) ColumnInfo(name = "faculty_id")]
    val facultyID: Long,
    val name: String
)
