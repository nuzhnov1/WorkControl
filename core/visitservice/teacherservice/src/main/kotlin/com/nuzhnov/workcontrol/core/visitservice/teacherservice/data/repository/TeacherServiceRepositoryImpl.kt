package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.repository

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.api.ControlServerApi
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.datasource.TeacherServiceLocalDataSource
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.datasource.VisitsRemoteDataSource
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.datasource.ParticipantsLocalDataSource
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.mapper.toParticipantActivityModel
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.mapper.toVisit
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.mapper.toServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.repository.TeacherServiceRepository
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceError
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.ApplicationCoroutineScope
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
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
            .onEach { visits -> onVisitsUpdate(visits) }
            .launchIn(scope = applicationCoroutineScope)
    }


    override fun updateServiceState(state: TeacherServiceState): Unit =
        teacherServiceLocalDataSource.updateServiceState(state)

    override fun updateLessonID(id: Long?): Unit =
        teacherServiceLocalDataSource.updateLessonID(id)

    override fun updateServiceName(name: String?): Unit =
        teacherServiceLocalDataSource.updateServiceName(name)

    override suspend fun startControl(): Unit = withContext(context = coroutineDispatcher) {
        when (val lessonID = teacherServiceLocalDataSource.lessonID.value) {
            null -> updateServiceState(
                state = TeacherServiceState.StoppedByError(
                    error = TeacherServiceError.INIT_ERROR
                )
            )

            else -> participantsLocalDataSource.getParticipants(lessonID)
                .onSuccess { participantEntityList ->
                    val visitArray= participantEntityList
                        .map(ParticipantEntity::toVisit)
                        .toTypedArray()

                    api.restoreVisits(*visitArray)
                    api.startServer()
                }
                .onFailure {
                    updateServiceState(
                        state = TeacherServiceState.StoppedByError(
                            error = TeacherServiceError.INIT_ERROR
                        )
                    )
                }
        }
    }

    private suspend fun onVisitsUpdate(visits: Set<Visit>): Result<Unit> = safeExecute {
        val lessonID = teacherServiceLocalDataSource.lessonID.value ?: return@safeExecute
        val lessonParticipantIDSet = participantsLocalDataSource.getParticipants(lessonID)
            .getOrThrow()
            .map { participantEntity -> participantEntity.studentID }
            .toSet()

        val participantActivityModelArray = visits
            .filter { visit ->
                if (visit.visitorID in lessonParticipantIDSet) {
                    true
                } else {
                    api.disconnectVisitor(visitorID = visit.visitorID)
                    false
                }
            }
            .map { visit -> visit.toParticipantActivityModel(lessonID) }
            .toTypedArray()

        participantsLocalDataSource.updateParticipantsActivities(*participantActivityModelArray)
    }


    private companion object {
        const val UPDATE_TIME_INTERVAL_MS = 100L
    }
}
