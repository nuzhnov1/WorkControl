package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.data.database.dao.FacultyDAO
import com.nuzhnov.workcontrol.core.data.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.data.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.data.mapper.toFacultyEntity
import com.nuzhnov.workcontrol.core.data.mapper.toGroupEntity
import com.nuzhnov.workcontrol.core.data.mapper.toStudentEntity
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
