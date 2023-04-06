package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitorApi
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorStateLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.mapper.toVisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState.*
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.InetAddress
import javax.inject.Inject

internal class VisitorServiceRepositoryImpl @Inject constructor(
    private val api: VisitorApi,
    visitorStateDataSource: VisitorStateLocalDataSource
) : VisitorServiceRepository {

    private val visitorState = visitorStateDataSource.visitorState

    private val _state = MutableStateFlow<VisitorServiceState>(value = NotInitialized)
    override val state = _state.asStateFlow()

    private val _discoveredServices = MutableStateFlow<Set<String>>(value = setOf())
    override val discoveredServices: Flow<Set<String>> = _discoveredServices


    override fun updateState(state: VisitorServiceState) {
        val currentState = _state.value
        val newState = state

        val currentStateIsStopped = currentState is Stopped || currentState is StoppedByError
        val newStateIsStopped = newState is Stopped || newState is StoppedByError

        if (currentStateIsStopped && newStateIsStopped) {
            return
        } else {
            _state.value = newState
        }
    }

    override fun addDiscoveredService(discoveredService: String) {
        _discoveredServices.value = _discoveredServices.value
            .toMutableSet()
            .apply { add(discoveredService) }
    }

    override fun removeDiscoveredService(discoveredService: String) {
        _discoveredServices.value = _discoveredServices.value
            .toMutableSet()
            .apply { remove(discoveredService) }
    }

    override fun clearDiscoveredServices() {
        _discoveredServices.value = setOf()
    }

    override suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) = coroutineScope {
        visitorState
            .onEach { visitorState -> updateState(state = visitorState.toVisitorServiceState()) }
            .launchIn(scope = this)

        api.startVisit(serverAddress, serverPort, visitorID)
        cancel()
    }
}
