package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlServerApiImpl
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorsRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.ServerStateLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.mapper.toVisitControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState.NotCreated
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitServiceCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.*
import javax.inject.Inject

internal class VisitControlServiceRepositoryImpl @Inject constructor(
    private val api: VisitControlServerApiImpl,
    visitorsDataSource: VisitorsRemoteDataSource,
    private val serverStateDataSource: ServerStateLocalDataSource,
    @VisitServiceCoroutineScope private val coroutineScope: CoroutineScope,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : VisitControlServiceRepository {

    private val _serviceState = MutableStateFlow<VisitControlServiceState>(value = NotCreated)
    override val serviceState = _serviceState.asStateFlow()

    override val visitors = visitorsDataSource.visitors


    override fun updateServiceState(serviceState: VisitControlServiceState) {
        _serviceState.value = serviceState
    }

    override fun startVisitControlServer(address: InetAddress, port: Int) {
        coroutineScope.launch(coroutineDispatcher) {
            launch {
                serverStateDataSource.serverState.collect { serverState ->
                    updateServiceState(serverState.toVisitControlServiceState())
                }
            }

            api.startServer(address, port)
            cancel()
        }
    }

    override fun clearVisitors() {
        api.clearVisitors()
    }
}
