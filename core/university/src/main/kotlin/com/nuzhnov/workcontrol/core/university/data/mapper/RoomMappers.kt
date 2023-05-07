package com.nuzhnov.workcontrol.core.university.data.mapper

import com.nuzhnov.workcontrol.core.model.Room
import com.nuzhnov.workcontrol.core.model.Building
import com.nuzhnov.workcontrol.core.api.dto.university.RoomDTO
import com.nuzhnov.workcontrol.core.database.entity.RoomEntity


internal fun RoomDTO.toRoom(building: Building): Room = Room(
    id = id,
    name = name,
    building = building
)

internal fun RoomEntity.toRoom(building: Building): Room = Room(
    id = id,
    name = name,
    building = building
)

internal fun Room.toRoomEntity(): RoomEntity = RoomEntity(
    id = id,
    name = name,
    buildingID = building.id
)
