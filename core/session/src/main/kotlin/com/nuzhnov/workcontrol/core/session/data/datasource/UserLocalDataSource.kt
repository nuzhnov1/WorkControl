package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.session.data.mapper.toUserData
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.data.database.entity.TeacherDisciplineCrossRefEntity
import com.nuzhnov.workcontrol.core.data.database.dao.*
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.models.Discipline
import com.nuzhnov.workcontrol.core.models.Role
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences,
    private val disciplineDAO: DisciplineDAO,
    private val departmentDAO: DepartmentDAO,
    private val groupDAO: GroupDAO,
    private val studentDAO: StudentDAO,
    private val teacherDAO: TeacherDAO,
    private val teacherDisciplineCrossRefDAO: TeacherDisciplineCrossRefDAO
) {

    fun getUserDataFlow() = flow {
        val session = appPreferences.getSession()

        when (session?.role) {
            Role.TEACHER -> teacherDAO
                .getTeacherFlow(id = session.id)
                .map { model -> model?.toUserData() }
                .collect { data -> emit(data) }

            Role.STUDENT -> studentDAO
                .getStudentFlow(id = session.id)
                .map { model -> model?.toUserData() }
                .collect { data -> emit(data) }

            else -> flow { emit(value = null) }
        }
    }

    suspend fun saveUserData(userData: UserData) {
        when (userData) {
            is UserData.TeacherData -> saveTeacherData(teacherData = userData)
            is UserData.StudentData -> saveStudentData(studentData = userData)
        }
    }

    suspend fun removeUserData() {
        val session = appPreferences.getSession() ?: return

        when (session.role) {
            Role.TEACHER -> removeTeacherData(teacherID = session.id)
            Role.STUDENT -> removeStudentData(studentID = session.id)
        }
    }

    private suspend fun saveTeacherData(teacherData: UserData.TeacherData) {
        val teacherEntity = teacherData.teacher.toTeacherEntity()

        val disciplineEntities = teacherData.disciplineList
            .map(Discipline::toDisciplineEntity)
            .toTypedArray()

        val teacherDisciplineCrossRefEntities = disciplineEntities.map { disciplineEntity ->
            TeacherDisciplineCrossRefEntity(
                teacherID = teacherEntity.id,
                disciplineID = disciplineEntity.id
            )
        }.toTypedArray()


        teacherDAO.insertOrUpdate(teacherEntity)
        disciplineDAO.insertOrUpdate(*disciplineEntities)
        teacherDisciplineCrossRefDAO.insertOrUpdate(*teacherDisciplineCrossRefEntities)
    }

    private suspend fun saveStudentData(studentData: UserData.StudentData) {
        val studentEntity = studentData.student.toStudentEntity()
        val groupEntity = studentData.student.group.toGroupEntity()
        val departmentEntity = studentData.student.group.department.toDepartmentEntity()

        departmentDAO.insertOrUpdate(departmentEntity)
        groupDAO.insertOrUpdate(groupEntity)
        studentDAO.insertOrUpdate(studentEntity)
    }

    private suspend fun removeTeacherData(teacherID: Long) {
        val teacherModel = teacherDAO.getTeacher(id = teacherID) ?: return
        val teacherEntity = teacherModel.teacherEntity
        val disciplineEntities = teacherModel.disciplineEntityList.toTypedArray()

        teacherDAO.delete(teacherEntity)
        disciplineDAO.delete(*disciplineEntities)
    }

    private suspend fun removeStudentData(studentID: Long) {
        val studentModel = studentDAO.getStudent(id = studentID) ?: return
        val studentEntity = studentModel.studentEntity
        val groupEntity = studentModel.groupModel.groupEntity
        val departmentEntity = studentModel.groupModel.departmentEntity

        studentDAO.delete(studentEntity)
        groupDAO.delete(groupEntity)
        departmentDAO.delete(departmentEntity)
    }
}
