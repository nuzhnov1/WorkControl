package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.work.data.mapper.toStudentEntity
import com.nuzhnov.workcontrol.core.work.data.mapper.toGroupEntity
import com.nuzhnov.workcontrol.core.work.data.mapper.toFacultyEntity
import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.database.dao.FacultyDAO
import javax.inject.Inject

internal class SyncStudentsWork @Inject constructor(
    private val syncService: SyncService,
    private val studentDAO: StudentDAO,
    private val groupDAO: GroupDAO,
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val studentIDList = studentDAO.getEntities().map { studentEntity -> studentEntity.id }

        if (studentIDList.isEmpty()) {
            return@runCatching
        }

        val studentModelDTOList = syncService.getStudents(studentIDList)

        studentModelDTOList
            .map { studentModelDTO -> studentModelDTO.groupModelDTO.facultyDTO }
            .map { facultyDTO -> facultyDTO.toFacultyEntity() }
            .run { facultyDAO.insertOrUpdate(*this.toTypedArray()) }

        studentModelDTOList
            .map { studentModelDTO -> studentModelDTO.groupModelDTO }
            .map { groupModelDTO -> groupModelDTO.toGroupEntity() }
            .run { groupDAO.insertOrUpdate(*this.toTypedArray()) }

        studentModelDTOList
            .map { studentModelDTO -> studentModelDTO.toStudentEntity() }
            .run { studentDAO.insertOrUpdate(*this.toTypedArray()) }
    }
}
