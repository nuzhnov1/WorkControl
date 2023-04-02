package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlClientApi
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.ClientStateLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.mapper.toVisitClientServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceState.NotInitialized
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitClientServiceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.InetAddress
import javax.inject.Inject

internal class VisitClientServiceRepositoryImpl @Inject constructor(
    private val api: VisitControlClientApi,
    private val clientStateDataSource: ClientStateLocalDataSource
) : VisitClientServiceRepository {

    private val _serviceState = MutableStateFlow<VisitClientServiceState>(value = NotInitialized)
    override val serviceState = _serviceState.asStateFlow()


    override fun updateServiceState(serviceState: VisitClientServiceState) {
        _serviceState.value = serviceState
    }

    override suspend fun startVisitClient(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: Long
    ) = coroutineScope {
        launch {
            clientStateDataSource.clientState.collect { clientState ->
                updateServiceState(clientState.toVisitClientServiceState())
            }
        }

        api.startClient(serverAddress, serverPort, visitorID)
        cancel()
    }
}
