package com.nuzhnov.workcontrol.shared.database.models

import com.nuzhnov.workcontrol.shared.database.entity.RoomEntity
import com.nuzhnov.workcontrol.shared.database.entity.BuildingEntity
import androidx.room.Embedded
import androidx.room.Relation

data class RoomWithBuilding(
    @Embedded
    val roomEntity: RoomEntity,

    @Relation(parentColumn = "building_id", entityColumn = "id")
    val buildingEntity: BuildingEntity
)
