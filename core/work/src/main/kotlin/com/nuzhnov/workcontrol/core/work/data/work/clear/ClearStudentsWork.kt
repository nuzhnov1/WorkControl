package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearStudentsWork @Inject constructor(
    private val studentDAO: StudentDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role == Role.STUDENT) {
            studentDAO.clear(session.id)
        } else {
            studentDAO.clear()
        }
    }
}
