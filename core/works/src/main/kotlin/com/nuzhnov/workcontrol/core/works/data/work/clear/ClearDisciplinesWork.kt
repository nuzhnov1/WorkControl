package com.nuzhnov.workcontrol.core.works.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.DisciplineDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearDisciplinesWork @Inject constructor(
    private val disciplineDAO: DisciplineDAO
) {

    suspend operator fun invoke() = safeExecute { disciplineDAO.clear() }
}
