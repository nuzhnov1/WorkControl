package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "department")
data class DepartmentEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String
)
