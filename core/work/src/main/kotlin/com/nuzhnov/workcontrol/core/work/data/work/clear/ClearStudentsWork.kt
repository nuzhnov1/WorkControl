package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.preferences.model.SessionPreferences.Role
import javax.inject.Inject

internal class ClearStudentsWork @Inject constructor(
    private val studentDAO: StudentDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        val sessionPreferences = appPreferences.getSessionPreferences()

        if (sessionPreferences?.role == Role.STUDENT) {
            studentDAO.clear(sessionPreferences.id)
        } else {
            studentDAO.clear()
        }
    }
}
