package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.work.data.mapper.toFacultyEntity
import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.database.dao.FacultyDAO
import javax.inject.Inject

internal class SyncFacultiesWork @Inject constructor(
    private val syncService: SyncService,
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val facultyIDList = facultyDAO.getEntities().map { facultyEntity -> facultyEntity.id }

        if (facultyIDList.isEmpty()) {
            return@runCatching
        }

        syncService.getFaculties(facultyIDList)
            .map { facultyDTO -> facultyDTO.toFacultyEntity() }
            .run { facultyDAO.insertOrUpdate(*this.toTypedArray()) }
    }
}
