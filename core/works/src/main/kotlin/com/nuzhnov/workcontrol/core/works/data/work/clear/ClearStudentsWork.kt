package com.nuzhnov.workcontrol.core.works.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.models.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearStudentsWork @Inject constructor(
    private val studentDAO: StudentDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke() = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role == Role.STUDENT) {
            studentDAO.clear(session.id)
        } else {
            studentDAO.clear()
        }
    }
}
