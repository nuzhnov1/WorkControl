package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.database.dao.FacultyDAO
import com.nuzhnov.workcontrol.core.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.mapper.toFacultyEntity
import com.nuzhnov.workcontrol.core.mapper.toGroupEntity
import com.nuzhnov.workcontrol.core.mapper.toStudentEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncStudentsWork @Inject constructor(
    private val syncService: SyncService,
    private val studentDAO: StudentDAO,
    private val groupDAO: GroupDAO,
    private val facultyDAO: FacultyDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val studentIDList = studentDAO.getEntities().map { studentEntity -> studentEntity.id }

        if (studentIDList.isEmpty()) {
            return@safeExecute
        }

        val studentModelDTOList = syncService.getStudents(studentIDList)

        val groupModelDTOList = studentModelDTOList
            .map { studentModelDTO -> studentModelDTO.groupModelDTO }
            .distinct()


        groupModelDTOList
            .map { studentModelDTO -> studentModelDTO.facultyDTO }
            .distinct()
            .map(FacultyDTO::toFacultyEntity)
            .toTypedArray()
            .let { entities -> facultyDAO.insertOrUpdate(*entities) }

        groupModelDTOList
            .map(GroupModelDTO::toGroupEntity)
            .toTypedArray()
            .let { entities -> groupDAO.insertOrUpdate(*entities) }

        studentModelDTOList
            .map(StudentModelDTO::toStudentEntity)
            .toTypedArray()
            .let { entities -> studentDAO.insertOrUpdate(*entities) }
    }
}
