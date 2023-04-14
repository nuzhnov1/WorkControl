package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.ControlServiceRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorsLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorsRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.util.throttleLatest
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.Visitor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class ControlServiceRepositoryImpl @Inject constructor(
    private val controlServiceRemoteDataSource: ControlServiceRemoteDataSource,
    private val visitorsLocalDataSource: VisitorsLocalDataSource,
    private val visitorsRemoteDataSource: VisitorsRemoteDataSource,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ControlServiceRepository {

    override val visitors = visitorsLocalDataSource.visitors
    override val serviceState = controlServiceRemoteDataSource.serviceState
    override val serviceName = controlServiceRemoteDataSource.serviceName


    override suspend fun getVisitors(): Set<Visitor> {
        return visitorsLocalDataSource.getVisitors()
    }

    override fun updateServiceState(state: ControlServiceState) {
        controlServiceRemoteDataSource.updateServiceState(state)
    }

    override fun updateServiceName(name: String?) {
        controlServiceRemoteDataSource.updateServiceName(name)
    }

    override suspend fun startControl() = withContext(coroutineDispatcher) {
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


    companion object {
        private const val UPDATE_TIME_INTERVAL_MS = 50L
    }
}
