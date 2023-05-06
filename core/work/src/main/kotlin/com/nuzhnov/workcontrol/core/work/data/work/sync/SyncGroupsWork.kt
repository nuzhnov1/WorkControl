package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.work.data.mapper.toGroupEntity
import com.nuzhnov.workcontrol.core.work.data.mapper.toFacultyEntity
import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.database.dao.FacultyDAO
import javax.inject.Inject

internal class SyncGroupsWork @Inject constructor(
    private val syncService: SyncService,
    private val groupDAO: GroupDAO,
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val groupIDList = groupDAO.getEntities().map { groupEntity -> groupEntity.id }

        if (groupIDList.isEmpty()) {
            return@runCatching
        }

        val groupModelDTOList = syncService.getGroups(groupIDList)

        groupModelDTOList
            .map { groupModelDTO -> groupModelDTO.facultyDTO }
            .map { facultyDTO -> facultyDTO.toFacultyEntity() }
            .run { facultyDAO.insertOrUpdate(*this.toTypedArray()) }

        groupModelDTOList
            .map { groupModelDTO -> groupModelDTO.toGroupEntity() }
            .run { groupDAO.insertOrUpdate(*this.toTypedArray()) }
    }
}
