package com.nuzhnov.workcontrol.core.data.database.entity.model

import com.nuzhnov.workcontrol.core.data.database.entity.RoomEntity
import com.nuzhnov.workcontrol.core.data.database.entity.BuildingEntity
import androidx.room.Embedded
import androidx.room.Relation

data class RoomEntityModel(
    @Embedded
    val roomEntity: RoomEntity,

    @Relation(parentColumn = "building_id", entityColumn = "id")
    val buildingEntity: BuildingEntity
)
