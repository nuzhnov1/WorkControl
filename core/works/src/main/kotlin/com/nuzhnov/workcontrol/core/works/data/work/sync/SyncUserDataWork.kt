package com.nuzhnov.workcontrol.core.works.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.UserService
import com.nuzhnov.workcontrol.core.data.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.data.database.dao.*
import com.nuzhnov.workcontrol.core.data.database.entity.TeacherDisciplineCrossRefEntity
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.models.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncUserDataWork @Inject constructor(
    private val userService: UserService,
    private val appPreferences: AppPreferences,
    private val studentDAO: StudentDAO,
    private val groupDAO: GroupDAO,
    private val departmentDAO: DepartmentDAO,
    private val teacherDAO: TeacherDAO,
    private val disciplineDAO: DisciplineDAO,
    private val teacherDisciplineCrossRefDAO: TeacherDisciplineCrossRefDAO
) {

    suspend operator fun invoke() = safeExecute {
        val currentRole = appPreferences.getSession()?.role ?: return@safeExecute

        when (currentRole) {
            Role.STUDENT -> syncStudentData()
            Role.TEACHER -> syncTeacherData()
        }
    }

    private suspend fun syncStudentData() = userService
        .getStudent()
        .let { modelDTO ->
            val departmentEntity = modelDTO.groupModelDTO.departmentDTO.toDepartmentEntity()
            val groupEntity = modelDTO.groupModelDTO.toGroupEntity()
            val studentEntity = modelDTO.toStudentEntity()

            departmentDAO.insertOrUpdate(departmentEntity)
            groupDAO.insertOrUpdate(groupEntity)
            studentDAO.insertOrUpdate(studentEntity)
        }

    private suspend fun syncTeacherData() = userService
        .getTeacher()
        .let { modelDTO ->
            val teacherEntity = modelDTO.toTeacherEntity()
            val disciplineEntities = modelDTO.disciplineDTOList
                .map(DisciplineDTO::toDisciplineEntity)
                .toTypedArray()

            val teacherDisciplineCrossRefEntities = disciplineEntities
                .map { entity ->
                    TeacherDisciplineCrossRefEntity(
                        teacherID = teacherEntity.id,
                        disciplineID = entity.id
                    )
                }
                .toTypedArray()

            teacherDAO.insertOrUpdate(teacherEntity)
            disciplineDAO.insertOrUpdate(*disciplineEntities)
            teacherDisciplineCrossRefDAO.insertOrUpdate(*teacherDisciplineCrossRefEntities)
        }
}
