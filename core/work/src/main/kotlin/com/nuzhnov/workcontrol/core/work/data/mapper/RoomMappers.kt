package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.university.RoomModelDTO
import com.nuzhnov.workcontrol.core.database.entity.RoomEntity


internal fun RoomModelDTO.toRoomEntity(): RoomEntity = RoomEntity(
    id = roomDTO.id,
    name = roomDTO.name,
    buildingID = buildingDTO.id
)
