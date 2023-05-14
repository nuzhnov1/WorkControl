package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discipline")
data class DisciplineEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String
)
