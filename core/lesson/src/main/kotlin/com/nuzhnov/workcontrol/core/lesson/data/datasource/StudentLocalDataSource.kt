package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.data.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import javax.inject.Inject

internal class StudentLocalDataSource @Inject constructor(
    private val studentDAO: StudentDAO
) {

    suspend fun getCurrentStudentModel(studentID: Long) = studentDAO.getStudent(studentID)

    suspend fun saveStudentEntities(vararg studentEntity: StudentEntity) = studentDAO.insertOrUpdate(*studentEntity)
}
