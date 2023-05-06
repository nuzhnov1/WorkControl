package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.work.data.mapper.toRoomEntity
import com.nuzhnov.workcontrol.core.work.data.mapper.toBuildingEntity
import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.database.dao.RoomDAO
import com.nuzhnov.workcontrol.core.database.dao.BuildingDAO
import javax.inject.Inject

internal class SyncRoomsWork @Inject constructor(
    private val syncService: SyncService,
    private val roomDAO: RoomDAO,
    private val buildingDAO: BuildingDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val roomIDList = roomDAO.getEntities().map { roomEntity -> roomEntity.id }

        if (roomIDList.isEmpty()) {
            return@runCatching
        }

        val roomModelDTOList = syncService.getRooms(roomIDList)

        roomModelDTOList
            .map { roomModelDTO -> roomModelDTO.buildingDTO }
            .map { buildingDTO -> buildingDTO.toBuildingEntity() }
            .run { buildingDAO.insertOrUpdate(*this.toTypedArray()) }

        roomModelDTOList
            .map { roomModelDTO -> roomModelDTO.toRoomEntity() }
            .run { roomDAO.insertOrUpdate(*this.toTypedArray()) }
    }
}
