package com.nuzhnov.workcontrol.core.teacherservice.data.repository

import com.nuzhnov.workcontrol.core.teacherservice.data.api.ControlServerApi
import com.nuzhnov.workcontrol.core.teacherservice.data.datasource.TeacherServiceLocalDataSource
import com.nuzhnov.workcontrol.core.teacherservice.data.datasource.VisitsRemoteDataSource
import com.nuzhnov.workcontrol.core.teacherservice.data.datasource.ParticipantsLocalDataSource
import com.nuzhnov.workcontrol.core.teacherservice.data.mapper.toParticipantActivitiesArray
import com.nuzhnov.workcontrol.core.teacherservice.data.mapper.toServiceState
import com.nuzhnov.workcontrol.core.teacherservice.data.mapper.toVisitArray
import com.nuzhnov.workcontrol.core.teacherservice.domen.repository.TeacherServiceRepository
import com.nuzhnov.workcontrol.core.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.util.di.annotation.ApplicationCoroutineScope
import com.nuzhnov.workcontrol.core.util.di.annotation.IODispatcher
import com.nuzhnov.workcontrol.common.util.throttleLatest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class TeacherServiceRepositoryImpl @Inject constructor(
    private val api: ControlServerApi,
    private val teacherServiceLocalDataSource: TeacherServiceLocalDataSource,
    visitsRemoteDataSource: VisitsRemoteDataSource,
    private val participantsLocalDataSource: ParticipantsLocalDataSource,
    @ApplicationCoroutineScope applicationCoroutineScope: CoroutineScope,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : TeacherServiceRepository {

    override val serviceState = teacherServiceLocalDataSource.serviceState
    override val serviceName = teacherServiceLocalDataSource.serviceName


    init {
        api.serverState
            .onEach { serverState ->
                updateServiceState(state = serverState.toServiceState() ?: return@onEach)
            }
            .launchIn(scope = applicationCoroutineScope)

        visitsRemoteDataSource.visitsFlow
            .throttleLatest(UPDATE_TIME_INTERVAL_MS)
            .onEach { visits ->
                val lessonID = teacherServiceLocalDataSource.lessonID.value ?: return@onEach
                val participantsArray = visits.toParticipantActivitiesArray(lessonID)

                participantsLocalDataSource.updateParticipantsActivities(participantsArray)
            }
            .launchIn(scope = applicationCoroutineScope)
    }


    override fun updateServiceState(state: TeacherServiceState) {
        teacherServiceLocalDataSource.updateServiceState(state)
    }

    override fun updateLessonID(id: Long?) {
        teacherServiceLocalDataSource.updateLessonID(id)
    }

    override fun updateServiceName(name: String?) {
        teacherServiceLocalDataSource.updateServiceName(name)
    }

    override suspend fun startControl() = withContext(context = coroutineDispatcher) {
        val persistedParticipants = participantsLocalDataSource.getParticipants()

        api.run {
            restoreVisits(visitsArray = persistedParticipants.toVisitArray())
            startServer()
        }
    }


    private companion object {
        const val UPDATE_TIME_INTERVAL_MS = 100L
    }
}
