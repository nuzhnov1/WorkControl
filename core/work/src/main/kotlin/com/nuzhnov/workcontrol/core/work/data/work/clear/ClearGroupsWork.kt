package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.GroupDAO
import javax.inject.Inject

internal class ClearGroupsWork @Inject constructor(
    private val groupDAO: GroupDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching { groupDAO.clear() }
}
