package com.nuzhnov.workcontrol.core.works.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.api.dto.university.RoomModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.data.database.dao.RoomDAO
import com.nuzhnov.workcontrol.core.data.database.dao.BuildingDAO
import com.nuzhnov.workcontrol.core.data.mapper.toRoomEntity
import com.nuzhnov.workcontrol.core.data.mapper.toBuildingEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncRoomsWork @Inject constructor(
    private val syncService: SyncService,
    private val roomDAO: RoomDAO,
    private val buildingDAO: BuildingDAO
) {

    suspend operator fun invoke() = safeExecute {
        val idList = roomDAO.getEntities().map { entity -> entity.id }

        if (idList.isEmpty()) {
            return@safeExecute
        }

        val dtoList = syncService.getRooms(idList)

        dtoList
            .map { modelDTO -> modelDTO.buildingDTO }
            .distinct()
            .map(BuildingDTO::toBuildingEntity)
            .toTypedArray()
            .let { entities -> buildingDAO.insertOrUpdate(*entities) }

        dtoList
            .map(RoomModelDTO::toRoomEntity)
            .toTypedArray()
            .let { entities -> roomDAO.insertOrUpdate(*entities) }
    }
}
