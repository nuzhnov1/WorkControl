package com.nuzhnov.workcontrol.core.works.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.api.dto.university.DepartmentDTO
import com.nuzhnov.workcontrol.core.data.database.dao.DepartmentDAO
import com.nuzhnov.workcontrol.core.data.mapper.toDepartmentEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncDepartmentsWork @Inject constructor(
    private val syncService: SyncService,
    private val departmentDAO: DepartmentDAO
) {

    suspend operator fun invoke() = safeExecute {
        val departmentIDList = departmentDAO.getEntities().map { entity -> entity.id }

        if (departmentIDList.isEmpty()) {
            return@safeExecute
        }

        syncService.getDepartments(departmentIDList)
            .map(DepartmentDTO::toDepartmentEntity)
            .toTypedArray()
            .let { departmentEntities -> departmentDAO.insertOrUpdate(*departmentEntities) }
    }
}
