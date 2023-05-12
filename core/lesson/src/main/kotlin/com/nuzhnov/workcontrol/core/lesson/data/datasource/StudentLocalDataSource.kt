package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.database.entity.model.StudentModel
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class StudentLocalDataSource @Inject constructor(
    private val studentDAO: StudentDAO,
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getCurrentStudentModel(): Result<StudentModel> =
        safeExecute(context = coroutineDispatcher) {
            val session = appPreferences.getSession()

            if (session?.role != Role.STUDENT) {
                throw IllegalStateException("permission denied")
            }

            studentDAO.getStudent(id = session.id) ?:
                throw IllegalStateException("student not found")
        }

    suspend fun saveStudentEntities(vararg studentEntity: StudentEntity): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            studentDAO.insertOrUpdate(*studentEntity)
        }
}
