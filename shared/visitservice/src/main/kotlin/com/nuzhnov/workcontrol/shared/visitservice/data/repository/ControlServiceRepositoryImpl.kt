package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.ControlServiceRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorsLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorsRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.util.throttleLatest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class ControlServiceRepositoryImpl @Inject constructor(
    private val controlServiceRemoteDataSource: ControlServiceRemoteDataSource,
    private val visitorsLocalDataSource: VisitorsLocalDataSource,
    private val visitorsRemoteDataSource: VisitorsRemoteDataSource
) : ControlServiceRepository {

    override val visitors = visitorsLocalDataSource.visitors
    override val serviceState = controlServiceRemoteDataSource.serviceState
    override val serviceName = controlServiceRemoteDataSource.serviceName


    override fun updateServiceState(state: ControlServiceState) {
        controlServiceRemoteDataSource.updateServiceState(state)
    }

    override fun updateServiceName(name: String?) {
        controlServiceRemoteDataSource.updateServiceName(name)
    }

    override suspend fun startControl() = coroutineScope {
        val visitorsUpdateJob = visitorsRemoteDataSource.visitors
            .throttleLatest(UPDATE_TIME_INTERVAL_MS)
            .onEach { visitors -> visitorsLocalDataSource.persistVisitors(visitors) }
            .launchIn(scope = this)

        controlServiceRemoteDataSource.run {
            restoreVisitors(visitors = visitorsLocalDataSource.getVisitors())
            startControl()
            visitorsUpdateJob.cancel()
        }
    }

    override suspend fun clearPersistVisitors() {
        visitorsLocalDataSource.clearPersistVisitors()
    }


    private companion object {
        const val UPDATE_TIME_INTERVAL_MS = 50L
    }
}
