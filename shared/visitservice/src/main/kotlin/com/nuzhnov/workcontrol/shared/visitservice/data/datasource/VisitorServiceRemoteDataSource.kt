package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.VisitorApi
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.mapper.toVisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState.*
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import java.net.InetAddress
import javax.inject.Inject

internal class VisitorServiceRemoteDataSource @Inject constructor(private val api: VisitorApi) {
    private val visitorState = api.visitorState

    private val _serviceState = MutableStateFlow<VisitorServiceState>(value = NotInitialized)
    val serviceState = _serviceState.asStateFlow()


    fun updateServiceState(state: VisitorServiceState) {
        val currentState = _serviceState.value

        val currentStateIsStopped = currentState is Stopped || currentState is StoppedByError
        val newStateIsStopped = state is Stopped || state is StoppedByError

        if (currentStateIsStopped && newStateIsStopped) {
            return
        } else {
            _serviceState.value = state
        }
    }

    suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) = coroutineScope {
        val serviceStateUpdateJob = visitorState
            .onEach { visitorState -> updateServiceState(visitorState.toVisitorServiceState()) }
            .launchIn(scope = this)

        api.startVisit(serverAddress, serverPort, visitorID)
        serviceStateUpdateJob.cancel()
    }
}
