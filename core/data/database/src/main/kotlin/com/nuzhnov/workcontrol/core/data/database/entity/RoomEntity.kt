package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "room",
    foreignKeys = [
        ForeignKey(
            entity = BuildingEntity::class,
            parentColumns = ["id"],
            childColumns = ["building_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class RoomEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    @ColumnInfo(name = "building_id", index = true) val buildingID: Long
)
