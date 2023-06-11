package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.DepartmentDTO
import com.nuzhnov.workcontrol.core.data.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.data.database.dao.DepartmentDAO
import com.nuzhnov.workcontrol.core.data.mapper.toGroupEntity
import com.nuzhnov.workcontrol.core.data.mapper.toDepartmentEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncGroupsWork @Inject constructor(
    private val syncService: SyncService,
    private val groupDAO: GroupDAO,
    private val departmentDAO: DepartmentDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val groupIDList = groupDAO.getEntities().map { groupEntity -> groupEntity.id }

        if (groupIDList.isEmpty()) {
            return@safeExecute
        }

        val groupModelDTOList = syncService.getGroups(groupIDList)

        groupModelDTOList
            .map { groupModelDTO -> groupModelDTO.departmentDTO }
            .distinct()
            .map(DepartmentDTO::toDepartmentEntity)
            .toTypedArray()
            .let { entities -> departmentDAO.insertOrUpdate(*entities) }

        groupModelDTOList
            .map(GroupModelDTO::toGroupEntity)
            .toTypedArray()
            .let { entities -> groupDAO.insertOrUpdate(*entities) }
    }
}
