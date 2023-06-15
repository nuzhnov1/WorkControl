package com.nuzhnov.workcontrol.core.works.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.api.dto.university.DepartmentDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.data.database.dao.DepartmentDAO
import com.nuzhnov.workcontrol.core.data.database.dao.GroupDAO
import com.nuzhnov.workcontrol.core.data.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.data.mapper.toDepartmentEntity
import com.nuzhnov.workcontrol.core.data.mapper.toGroupEntity
import com.nuzhnov.workcontrol.core.data.mapper.toStudentEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncStudentsWork @Inject constructor(
    private val syncService: SyncService,
    private val studentDAO: StudentDAO,
    private val groupDAO: GroupDAO,
    private val departmentDAO: DepartmentDAO
) {

    suspend operator fun invoke() = safeExecute {
        val idList = studentDAO.getEntities().map { entity -> entity.id }

        if (idList.isEmpty()) {
            return@safeExecute
        }

        val studentModelDTOList = syncService.getStudents(idList)

        val groupModelDTOList = studentModelDTOList
            .map { modelDTO -> modelDTO.groupModelDTO }
            .distinct()


        groupModelDTOList
            .map { modelDTO -> modelDTO.departmentDTO }
            .distinct()
            .map(DepartmentDTO::toDepartmentEntity)
            .toTypedArray()
            .let { entities -> departmentDAO.insertOrUpdate(*entities) }

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
