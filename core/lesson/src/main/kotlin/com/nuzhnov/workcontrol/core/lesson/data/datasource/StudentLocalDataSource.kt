package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.data.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.StudentModel
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class StudentLocalDataSource @Inject constructor(
    private val studentDAO: StudentDAO,
    private val appPreferences: AppPreferences
) {

    suspend fun getCurrentStudentModel(): Result<StudentModel> = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role != Role.STUDENT) {
            throw IllegalStateException("permission denied")
        }

        studentDAO.getStudent(id = session.id)
            ?: throw IllegalStateException("student not found")
    }

    suspend fun saveStudentEntities(
        vararg studentEntity: StudentEntity
    ): Result<Unit> = safeExecute { studentDAO.insertOrUpdate(*studentEntity) }
}
