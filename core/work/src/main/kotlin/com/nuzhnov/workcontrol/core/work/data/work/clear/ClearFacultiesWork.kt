package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.FacultyDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearFacultiesWork @Inject constructor(
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute { facultyDAO.clear() }
}
