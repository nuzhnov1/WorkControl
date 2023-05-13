package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.session.data.mapper.toUserData
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.data.database.entity.TeacherDisciplineCrossRefEntity
import com.nuzhnov.workcontrol.core.data.database.dao.*
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
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

    fun getUserDataFlow(): Flow<UserData?> = appPreferences
        .getSessionFlow()
        .transform { session ->
            when (session?.role) {
                Role.TEACHER -> teacherDAO
                    .getTeacherFlow(id = session.id)
                    .map { teacherModel -> teacherModel?.toUserData() }
                    .flowOn(context = coroutineDispatcher)
                    .collect { userData -> emit(userData) }

                Role.STUDENT -> studentDAO
                    .getStudentFlow(id = session.id)
                    .map { studentModel -> studentModel?.toUserData() }
                    .flowOn(context = coroutineDispatcher)
                    .collect { userData -> emit(userData) }

                null -> emit(value = null)
            }
        }
        .flowOn(context = coroutineDispatcher)

    suspend fun saveUserData(userData: UserData): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            when (userData) {
                is UserData.TeacherData -> saveTeacherData(teacherData = userData)
                is UserData.StudentData -> saveStudentData(studentData = userData)
            }
        }

    suspend fun removeUserData(): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            val session = appPreferences.getSession() ?: return@safeExecute
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
