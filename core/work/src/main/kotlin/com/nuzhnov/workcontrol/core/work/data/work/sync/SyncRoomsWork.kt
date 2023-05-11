package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.api.dto.university.RoomModelDTO
import com.nuzhnov.workcontrol.core.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.database.dao.RoomDAO
import com.nuzhnov.workcontrol.core.database.dao.BuildingDAO
import com.nuzhnov.workcontrol.core.mapper.toRoomEntity
import com.nuzhnov.workcontrol.core.mapper.toBuildingEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncRoomsWork @Inject constructor(
    private val syncService: SyncService,
    private val roomDAO: RoomDAO,
    private val buildingDAO: BuildingDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val roomIDList = roomDAO.getEntities().map { roomEntity -> roomEntity.id }

        if (roomIDList.isEmpty()) {
            return@safeExecute
        }

        val roomModelDTOList = syncService.getRooms(roomIDList)

        roomModelDTOList
            .map { roomModelDTO -> roomModelDTO.buildingDTO }
            .distinct()
            .map(BuildingDTO::toBuildingEntity)
            .toTypedArray()
            .let { entities -> buildingDAO.insertOrUpdate(*entities) }

        roomModelDTOList
            .map(RoomModelDTO::toRoomEntity)
            .toTypedArray()
            .let { entities -> roomDAO.insertOrUpdate(*entities) }
    }
}
