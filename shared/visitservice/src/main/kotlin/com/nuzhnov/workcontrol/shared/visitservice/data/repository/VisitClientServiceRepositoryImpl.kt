package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlClientApi
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.ClientStateLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.mapper.toVisitClientServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceState.NotCreated
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitClientServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitServiceCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.InetAddress
import javax.inject.Inject

internal class VisitClientServiceRepositoryImpl @Inject constructor(
    private val api: VisitControlClientApi,
    private val clientStateDataSource: ClientStateLocalDataSource,
    @VisitServiceCoroutineScope private val coroutineScope: CoroutineScope,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : VisitClientServiceRepository {

    private val _serviceState = MutableStateFlow<VisitClientServiceState>(value = NotCreated)
    override val serviceState = _serviceState.asStateFlow()


    override fun updateServiceState(serviceState: VisitClientServiceState) {
        _serviceState.value = serviceState
    }

    override fun startVisitClient(serverAddress: InetAddress, serverPort: Int, visitorID: Long) {
        coroutineScope.launch(coroutineDispatcher) {
            launch {
                clientStateDataSource.clientState.collect { clientState ->
                    updateServiceState(clientState.toVisitClientServiceState())
                }
            }

            api.startClient(serverAddress, serverPort, visitorID)
            cancel()
        }
    }
}
