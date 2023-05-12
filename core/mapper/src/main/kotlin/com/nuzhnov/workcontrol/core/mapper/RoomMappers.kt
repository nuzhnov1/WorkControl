package com.nuzhnov.workcontrol.core.mapper

import com.nuzhnov.workcontrol.core.model.Room
import com.nuzhnov.workcontrol.core.model.Building
import com.nuzhnov.workcontrol.core.api.dto.university.RoomDTO
import com.nuzhnov.workcontrol.core.api.dto.university.RoomModelDTO
import com.nuzhnov.workcontrol.core.database.entity.RoomEntity
import com.nuzhnov.workcontrol.core.database.entity.model.RoomModel


fun RoomDTO.toRoom(building: Building): Room = Room(
    id = id,
    name = name,
    building = building
)

fun RoomDTO.toRoomEntity(buildingID: Long): RoomEntity = RoomEntity(
    id = id,
    name = name,
    buildingID = buildingID
)

fun RoomModelDTO.toRoom(): Room = Room(
    id = roomDTO.id,
    name = roomDTO.name,
    building = buildingDTO.toBuilding()
)

fun RoomModelDTO.toRoomModel(): RoomModel = RoomModel(
    roomEntity = roomDTO.toRoomEntity(buildingID = buildingDTO.id),
    buildingEntity = buildingDTO.toBuildingEntity()
)

fun RoomModelDTO.toRoomEntity(): RoomEntity = RoomEntity(
    id = roomDTO.id,
    name = roomDTO.name,
    buildingID = buildingDTO.id
)

fun RoomEntity.toRoom(building: Building): Room = Room(
    id = id,
    name = name,
    building = building
)

fun RoomModel.toRoom(): Room = Room(
    id = roomEntity.id,
    name = roomEntity.name,
    building = buildingEntity.toBuilding()
)

fun Room.toRoomEntity(): RoomEntity = RoomEntity(
    id = id,
    name = name,
    buildingID = building.id
)

fun Room.toRoomModel(): RoomModel = RoomModel(
    roomEntity = this.toRoomEntity(),
    buildingEntity = building.toBuildingEntity()
)
