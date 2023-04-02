package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlServerApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorsRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.ServerStateLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.mapper.toVisitControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState.NotInitialized
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitControlServiceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.*
import javax.inject.Inject

internal class VisitControlServiceRepositoryImpl @Inject constructor(
    private val api: VisitControlServerApiImpl,
    visitorsDataSource: VisitorsRemoteDataSource,
    private val serverStateDataSource: ServerStateLocalDataSource
) : VisitControlServiceRepository {

    private val _serviceState = MutableStateFlow<VisitControlServiceState>(value = NotInitialized)
    override val serviceState = _serviceState.asStateFlow()

    override val visitors = visitorsDataSource.visitors

    override fun updateServiceState(serviceState: VisitControlServiceState) {
        _serviceState.value = serviceState
    }

    @OptIn(FlowPreview::class)
    override suspend fun startVisitControlServer() = coroutineScope {
        launch {
            serverStateDataSource.serverState.collect { serverState ->
                updateServiceState(serverState.toVisitControlServiceState())
            }
        }

        launch {
            visitors.debounce(UPDATE_TIME_INTERVAL_MS).collect { visitors ->
                // TODO: update data in the Participant table
            }
        }

        api.startServer()
        cancel()
    }


    companion object {
        const val UPDATE_TIME_INTERVAL_MS = 500L
    }
}
