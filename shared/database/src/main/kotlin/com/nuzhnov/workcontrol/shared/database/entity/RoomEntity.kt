package com.nuzhnov.workcontrol.shared.database.entity

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
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class RoomEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    @ColumnInfo(name = "building_id", index = true) val buildingID: Long
)
