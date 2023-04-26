package com.nuzhnov.workcontrol.core.database.models

import com.nuzhnov.workcontrol.core.database.entity.RoomEntity
import com.nuzhnov.workcontrol.core.database.entity.BuildingEntity
import androidx.room.Embedded
import androidx.room.Relation

data class RoomWithBuilding(
    @Embedded
    val roomEntity: RoomEntity,

    @Relation(parentColumn = "building_id", entityColumn = "id")
    val buildingEntity: BuildingEntity
)
