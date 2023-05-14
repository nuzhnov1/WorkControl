package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building")
data class BuildingEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String
)
