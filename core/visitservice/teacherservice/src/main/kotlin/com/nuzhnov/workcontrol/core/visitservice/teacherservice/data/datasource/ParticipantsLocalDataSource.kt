package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.datasource

import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.update.ParticipantActivityModel
import com.nuzhnov.workcontrol.core.data.database.dao.ParticipantDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class ParticipantsLocalDataSource @Inject constructor(
    private val participantDAO: ParticipantDAO,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getParticipants(
        lessonID: Long
    ): Result<List<ParticipantEntity>> = safeExecute(context = coroutineDispatcher) {
        participantDAO.getEntities(lessonID)
    }

    suspend fun updateParticipantsActivities(
        vararg participantActivityModel: ParticipantActivityModel
    ): Result<Unit> = safeExecute(context = coroutineDispatcher) {
        participantDAO.updateActivity(*participantActivityModel)
    }
}
