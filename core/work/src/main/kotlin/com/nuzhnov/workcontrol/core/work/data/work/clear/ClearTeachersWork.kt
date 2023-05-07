package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.TeacherDAO
import com.nuzhnov.workcontrol.core.database.util.safeTransactionExecute
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import javax.inject.Inject

internal class ClearTeachersWork @Inject constructor(
    private val teacherDAO: TeacherDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke(): Result<Unit> = safeTransactionExecute {
        val session = appPreferences.getSession()

        if (session?.role == Role.TEACHER) {
            teacherDAO.clear(session.id)
        } else {
            teacherDAO.clear()
        }
    }
}
