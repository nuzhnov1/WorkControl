package com.nuzhnov.workcontrol.core.database.entity.model

import com.nuzhnov.workcontrol.core.database.entity.RoomEntity
import com.nuzhnov.workcontrol.core.database.entity.BuildingEntity
import androidx.room.Embedded
import androidx.room.Relation

data class RoomModel(
    @Embedded
    val roomEntity: RoomEntity,

    @Relation(parentColumn = "building_id", entityColumn = "id")
    val buildingEntity: BuildingEntity
)
