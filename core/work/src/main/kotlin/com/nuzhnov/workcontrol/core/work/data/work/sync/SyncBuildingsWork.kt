package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.database.dao.BuildingDAO
import com.nuzhnov.workcontrol.core.mapper.toBuildingEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncBuildingsWork @Inject constructor(
    private val syncService: SyncService,
    private val buildingDAO: BuildingDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val buildingIDList = buildingDAO.getEntities().map { buildingEntity -> buildingEntity.id }

        if (buildingIDList.isEmpty()) {
            return@safeExecute
        }

        syncService.getBuildings(buildingIDList)
            .map(BuildingDTO::toBuildingEntity)
            .toTypedArray()
            .let { entities -> buildingDAO.insertOrUpdate(*entities) }
    }
}
