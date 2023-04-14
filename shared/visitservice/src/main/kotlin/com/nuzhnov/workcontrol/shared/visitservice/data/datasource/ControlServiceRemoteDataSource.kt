package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.ControlServerApi
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.mapper.toNetworkModelSet
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.mapper.toControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.Visitor
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class ControlServiceRemoteDataSource @Inject constructor(
    private val api: ControlServerApi,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    private val serverState = api.controlServerState

    private val _serviceState = MutableStateFlow<ControlServiceState>(value = NotInitialized)
    val serviceState = _serviceState.asStateFlow()

    private val _serviceName = MutableStateFlow<String?>(value = null)
    val serviceName = _serviceName.asStateFlow()


    fun updateServiceState(state: ControlServiceState) {
        val currentState = _serviceState.value

        val currentStateIsStopped = currentState is Stopped || currentState is StoppedByError
        val newStateIsStopped = state is Stopped || state is StoppedByError

        if (currentStateIsStopped && newStateIsStopped) {
            return
        } else {
            _serviceState.value = state
        }
    }

    fun updateServiceName(name: String?) {
        _serviceName.value = name
    }

    suspend fun startControl() = withContext(coroutineDispatcher) {
        val serviceStateUpdateJob = serverState
            .onEach { serverState ->
                updateServiceState(state = serverState.toControlServiceState() ?: return@onEach)
            }
            .launchIn(scope = this)

        api.startControl()
        serviceStateUpdateJob.cancel()
    }

    fun restoreVisitors(visitors: Iterable<Visitor>) {
        api.restoreVisitors(visitors = visitors.toNetworkModelSet())
    }
}
