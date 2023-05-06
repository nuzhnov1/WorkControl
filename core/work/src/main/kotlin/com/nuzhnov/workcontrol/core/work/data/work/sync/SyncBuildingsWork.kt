package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.work.data.mapper.toBuildingEntity
import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.database.dao.BuildingDAO
import javax.inject.Inject

internal class SyncBuildingsWork @Inject constructor(
    private val syncService: SyncService,
    private val buildingDAO: BuildingDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val buildingIDList = buildingDAO.getEntities().map { buildingEntity -> buildingEntity.id }

        if (buildingIDList.isEmpty()) {
            return@runCatching
        }

        syncService.getBuildings(buildingIDList)
            .map { buildingDTO -> buildingDTO.toBuildingEntity() }
            .run { buildingDAO.insertOrUpdate(*this.toTypedArray()) }
    }
}
