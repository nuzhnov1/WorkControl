package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.RoomDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearRoomsWork @Inject constructor(
    private val roomDAO: RoomDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute { roomDAO.clear() }
}
