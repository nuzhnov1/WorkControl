package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.BuildingDAO
import com.nuzhnov.workcontrol.core.database.util.safeTransactionExecute
import javax.inject.Inject

internal class ClearBuildingsWork @Inject constructor(
    private val buildingDAO: BuildingDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeTransactionExecute { buildingDAO.clear() }
}
