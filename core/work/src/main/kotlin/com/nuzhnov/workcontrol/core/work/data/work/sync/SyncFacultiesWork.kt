package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.data.database.dao.FacultyDAO
import com.nuzhnov.workcontrol.core.data.mapper.toFacultyEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncFacultiesWork @Inject constructor(
    private val syncService: SyncService,
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val facultyIDList = facultyDAO.getEntities().map { facultyEntity -> facultyEntity.id }

        if (facultyIDList.isEmpty()) {
            return@safeExecute
        }

        syncService.getFaculties(facultyIDList)
            .map(FacultyDTO::toFacultyEntity)
            .toTypedArray()
            .let { entities -> facultyDAO.insertOrUpdate(*entities) }
    }
}
