package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.data.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.data.database.dao.FacultyDAO
import com.nuzhnov.workcontrol.core.data.mapper.toGroupEntity
import com.nuzhnov.workcontrol.core.data.mapper.toFacultyEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncGroupsWork @Inject constructor(
    private val syncService: SyncService,
    private val groupDAO: GroupDAO,
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val groupIDList = groupDAO.getEntities().map { groupEntity -> groupEntity.id }

        if (groupIDList.isEmpty()) {
            return@safeExecute
        }

        val groupModelDTOList = syncService.getGroups(groupIDList)

        groupModelDTOList
            .map { groupModelDTO -> groupModelDTO.facultyDTO }
            .distinct()
            .map(FacultyDTO::toFacultyEntity)
            .toTypedArray()
            .let { entities -> facultyDAO.insertOrUpdate(*entities) }

        groupModelDTOList
            .map(GroupModelDTO::toGroupEntity)
            .toTypedArray()
            .let { entities -> groupDAO.insertOrUpdate(*entities) }
    }
}
