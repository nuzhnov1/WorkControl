package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.RoomDAO
import javax.inject.Inject

internal class ClearRoomsWork @Inject constructor(
    private val roomDAO: RoomDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching { roomDAO.clear() }
}
