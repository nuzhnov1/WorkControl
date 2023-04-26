package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "building")
data class BuildingEntity(
    @[PrimaryKey(autoGenerate = false) ColumnInfo(name = "building_id")]
    val buildingID: Long,
    val name: String,
    val address: String
)
