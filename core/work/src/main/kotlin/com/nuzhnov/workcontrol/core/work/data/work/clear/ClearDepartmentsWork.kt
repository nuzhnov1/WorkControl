package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.DepartmentDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearDepartmentsWork @Inject constructor(
    private val departmentDAO: DepartmentDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute { departmentDAO.clear() }
}
