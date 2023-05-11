package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.api.service.UserService
import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.database.dao.*
import com.nuzhnov.workcontrol.core.database.entity.TeacherDisciplineCrossRefEntity
import com.nuzhnov.workcontrol.core.mapper.*
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncUserDataWork @Inject constructor(
    private val userService: UserService,
    private val appPreferences: AppPreferences,
    private val studentDAO: StudentDAO,
    private val groupDAO: GroupDAO,
    private val facultyDAO: FacultyDAO,
    private val teacherDAO: TeacherDAO,
    private val disciplineDAO: DisciplineDAO,
    private val teacherDisciplineCrossRefDAO: TeacherDisciplineCrossRefDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val currentRole = appPreferences.getSession()?.role ?: return@safeExecute

        when (currentRole) {
            Role.STUDENT -> syncStudentData()
            Role.TEACHER -> syncTeacherData()
        }
    }

    private suspend fun syncStudentData(): Unit = userService
        .getStudent()
        .let { studentModelDTO ->
            val facultyEntity = studentModelDTO.groupModelDTO.facultyDTO.toFacultyEntity()
            val groupEntity = studentModelDTO.groupModelDTO.toGroupEntity()
            val studentEntity = studentModelDTO.toStudentEntity()

            facultyDAO.insertOrUpdate(facultyEntity)
            groupDAO.insertOrUpdate(groupEntity)
            studentDAO.insertOrUpdate(studentEntity)
        }

    private suspend fun syncTeacherData(): Unit = userService
        .getTeacher()
        .let { teacherModelDTO ->
            val teacherEntity = teacherModelDTO.toTeacherEntity()
            val disciplineEntityArray = teacherModelDTO.disciplineDTOList
                .map(DisciplineDTO::toDisciplineEntity)
                .toTypedArray()

            val teacherDisciplineCrossRefEntityArray = disciplineEntityArray
                .map { disciplineEntity ->
                    TeacherDisciplineCrossRefEntity(
                        teacherID = teacherEntity.id,
                        disciplineID = disciplineEntity.id
                    )
                }
                .toTypedArray()

            teacherDAO.insertOrUpdate(teacherEntity)
            disciplineDAO.insertOrUpdate(*disciplineEntityArray)
            teacherDisciplineCrossRefDAO.insertOrUpdate(*teacherDisciplineCrossRefEntityArray)
        }
}
