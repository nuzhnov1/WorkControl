package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.TeacherDAO
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.preferences.model.SessionPreferences.Role
import javax.inject.Inject

internal class ClearTeachersWork @Inject constructor(
    private val teacherDAO: TeacherDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val sessionPreferences = appPreferences.getSessionPreferences()

        if (sessionPreferences?.role == Role.TEACHER) {
            teacherDAO.clear(sessionPreferences.id)
        } else {
            teacherDAO.clear()
        }
    }
}
