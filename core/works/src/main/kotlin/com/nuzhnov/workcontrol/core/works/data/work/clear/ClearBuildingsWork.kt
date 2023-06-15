package com.nuzhnov.workcontrol.core.works.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.BuildingDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearBuildingsWork @Inject constructor(
    private val buildingDAO: BuildingDAO
) {

    suspend operator fun invoke() = safeExecute { buildingDAO.clear() }
}
