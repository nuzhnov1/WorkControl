package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.session.data.mapper.*
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.database.entity.TeacherDisciplineCrossRefEntity
import com.nuzhnov.workcontrol.core.database.dao.*
import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UserLocalDataSource @Inject constructor(
    private val appPreferences: AppPreferences,
    private val disciplineDAO: DisciplineDAO,
    private val facultyDAO: FacultyDAO,
    private val groupDAO: GroupDAO,
    private val studentDAO: StudentDAO,
    private val teacherDAO: TeacherDAO,
    private val teacherDisciplineCrossRefDAO: TeacherDisciplineCrossRefDAO,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getUserData(): UserData? =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.getSession() ?: return@withContext null
            val sessionID = session.id

            when (session.role) {
                Role.TEACHER -> teacherDAO.getTeacher(id = sessionID)?.toUserData()
                Role.STUDENT -> studentDAO.getStudent(id = sessionID)?.toUserData()
            }
        }

    suspend fun saveUserData(userData: UserData): Unit =
        withContext(context = coroutineDispatcher) {
            when (userData) {
                is UserData.TeacherData -> saveTeacherData(teacherData = userData)
                is UserData.StudentData -> saveStudentData(studentData = userData)
            }
        }

    suspend fun removeUserData(): Unit =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.getSession() ?: return@withContext
            val sessionID = session.id

            when (session.role) {
                Role.TEACHER -> removeTeacherData(teacherID = sessionID)
                Role.STUDENT -> removeStudentData(studentID = sessionID)
            }
        }

    private suspend fun saveTeacherData(teacherData: UserData.TeacherData) {
        val teacherEntity = teacherData.teacher.toTeacherEntity()

        val disciplineEntityArray = teacherData.disciplineList
            .map(Discipline::toDisciplineEntity)
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

    private suspend fun saveStudentData(studentData: UserData.StudentData) {
        val studentEntity = studentData.student.toStudentEntity()
        val groupEntity = studentData.student.group.toGroupEntity()
        val facultyEntity = studentData.student.group.faculty.toFacultyEntity()

        facultyDAO.insertOrUpdate(facultyEntity)
        groupDAO.insertOrUpdate(groupEntity)
        studentDAO.insertOrUpdate(studentEntity)
    }

    private suspend fun removeTeacherData(teacherID: Long) {
        val teacherModel = teacherDAO.getTeacher(id = teacherID) ?: return
        val teacherEntity = teacherModel.teacherEntity
        val disciplineEntityArray = teacherModel.disciplineEntityList.toTypedArray()

        teacherDAO.delete(teacherEntity)
        disciplineDAO.delete(*disciplineEntityArray)
    }

    private suspend fun removeStudentData(studentID: Long) {
        val studentModel = studentDAO.getStudent(id = studentID) ?: return
        val studentEntity = studentModel.studentEntity
        val groupEntity = studentModel.groupModel.groupEntity
        val facultyEntity = studentModel.groupModel.facultyEntity

        studentDAO.delete(studentEntity)
        groupDAO.delete(groupEntity)
        facultyDAO.delete(facultyEntity)
    }
}