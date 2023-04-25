package com.nuzhnov.workcontrol.core.teacherservice.data.datasource

import com.nuzhnov.workcontrol.core.util.di.annotation.IODispatcher
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.models.ParticipantActivity
import com.nuzhnov.workcontrol.core.database.dao.ParticipantDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ParticipantsLocalDataSource @Inject constructor(
    private val participantDao: ParticipantDao,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getParticipants(): List<ParticipantEntity> = withContext(coroutineDispatcher) {
        participantDao.getEntities()
    }

    suspend fun updateParticipantsActivities(participantsActivities: Array<ParticipantActivity>) {
        participantDao.updateActivity(*participantsActivities)
    }
}
