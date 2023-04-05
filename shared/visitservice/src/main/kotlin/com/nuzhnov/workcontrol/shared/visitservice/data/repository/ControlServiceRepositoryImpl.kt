package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.api.ControlServerApi
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitsRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.ControlServerStateLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.mapper.toControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.util.throttleLatest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class ControlServiceRepositoryImpl @Inject constructor(
    private val api: ControlServerApi,
    visitsDataSource: VisitsRemoteDataSource,
    controlServerStateDataSource: ControlServerStateLocalDataSource
) : ControlServiceRepository {

    private val controlServerState = controlServerStateDataSource.controlServerState
    private val visits = visitsDataSource.visits

    private val _state = MutableStateFlow<ControlServiceState>(value = NotInitialized)
    override val state = _state.asStateFlow()

    private val _name = MutableStateFlow<String?>(value = null)
    override val name = _name.asStateFlow()


    override fun updateState(state: ControlServiceState) {
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

    override fun updateName(name: String?) {
        _name.value = name
    }

    override suspend fun startControl() = coroutineScope {
        controlServerState
            .onEach { serverState -> updateState(state = serverState.toControlServiceState()) }
            .launchIn(scope = this)

        visits
            .throttleLatest(UPDATE_TIME_INTERVAL_MS)
            .onEach { visits -> /* TODO: update data in the Visitor table */ }
            .launchIn(scope = this)

        api.startControl()
        cancel()
    }


    companion object {
        const val UPDATE_TIME_INTERVAL_MS = 500L
    }
}
