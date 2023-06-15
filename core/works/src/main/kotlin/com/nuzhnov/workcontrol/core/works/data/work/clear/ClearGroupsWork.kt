package com.nuzhnov.workcontrol.core.works.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearGroupsWork @Inject constructor(
    private val groupDAO: GroupDAO
) {

    suspend operator fun invoke() = safeExecute { groupDAO.clear() }
}
