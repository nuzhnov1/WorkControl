package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.TeacherDAO
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearTeachersWork @Inject constructor(
    private val teacherDAO: TeacherDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role == Role.TEACHER) {
            teacherDAO.clear(session.id)
        } else {
            teacherDAO.clear()
        }
    }
}
