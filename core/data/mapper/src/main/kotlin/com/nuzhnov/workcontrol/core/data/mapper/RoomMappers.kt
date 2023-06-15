package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Room
import com.nuzhnov.workcontrol.core.models.Building
import com.nuzhnov.workcontrol.core.data.api.dto.university.RoomDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.RoomModelDTO
import com.nuzhnov.workcontrol.core.data.database.entity.RoomEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.RoomModel


fun RoomDTO.toRoomEntity(buildingID: Long) = RoomEntity(
    id = id,
    name = name,
    buildingID = buildingID
)

fun RoomModelDTO.toRoomModel() = RoomModel(
    roomEntity = roomDTO.toRoomEntity(buildingID = buildingDTO.id),
    buildingEntity = buildingDTO.toBuildingEntity()
)

fun RoomEntity.toRoom(building: Building) = Room(
    id = id,
    name = name,
    building = building
)

fun RoomModel.toRoom() = Room(
    id = roomEntity.id,
    name = roomEntity.name,
    building = buildingEntity.toBuilding()
)

fun Room.toRoomEntity() = RoomEntity(
    id = id,
    name = name,
    buildingID = building.id
)

fun Room.toRoomModel() = RoomModel(
    roomEntity = this.toRoomEntity(),
    buildingEntity = building.toBuildingEntity()
)
