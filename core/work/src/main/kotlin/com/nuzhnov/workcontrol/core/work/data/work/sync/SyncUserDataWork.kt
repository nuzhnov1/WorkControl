package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.api.service.UserService
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.database.dao.*
import com.nuzhnov.workcontrol.core.database.entity.TeacherDisciplineCrossRefEntity
import com.nuzhnov.workcontrol.core.work.data.mapper.*
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

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val currentRole = appPreferences.getSession()?.role ?: return@runCatching

        when (currentRole) {
            Role.STUDENT -> syncStudentData()
            Role.TEACHER -> syncTeacherData()
        }
    }

    private suspend fun syncStudentData(): Unit = userService.getStudent().run {
        val facultyEntity = this.groupModelDTO.facultyDTO.toFacultyEntity()
        val groupEntity = this.groupModelDTO.toGroupEntity()
        val studentEntity = this.toStudentEntity()

        facultyDAO.insertOrUpdate(facultyEntity)
        groupDAO.insertOrUpdate(groupEntity)
        studentDAO.insertOrUpdate(studentEntity)
    }

    private suspend fun syncTeacherData(): Unit = userService.getTeacher().run {
        val teacherEntity = this.toTeacherEntity()
        val disciplineEntityArray = this.disciplineDTOList
            .map { disciplineDTO -> disciplineDTO.toDisciplineEntity() }
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
