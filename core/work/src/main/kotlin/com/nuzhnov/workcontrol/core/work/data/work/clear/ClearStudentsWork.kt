package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.database.util.safeTransactionExecute
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.model.Role
import javax.inject.Inject

internal class ClearStudentsWork @Inject constructor(
    private val studentDAO: StudentDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke(): Result<Unit> = safeTransactionExecute {
        val session = appPreferences.getSession()

        if (session?.role == Role.STUDENT) {
            studentDAO.clear(session.id)
        } else {
            studentDAO.clear()
        }
    }
}
