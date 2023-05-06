package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.FacultyDAO
import javax.inject.Inject

internal class ClearFacultiesWork @Inject constructor(
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching { facultyDAO.clear() }
}
