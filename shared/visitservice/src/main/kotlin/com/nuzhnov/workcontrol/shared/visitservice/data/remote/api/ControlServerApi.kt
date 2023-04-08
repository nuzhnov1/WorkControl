package com.nuzhnov.workcontrol.shared.visitservice.data.remote.api

import com.nuzhnov.workcontrol.shared.visitservice.data.remote.util.VisitorNetworkModel
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface ControlServerApi {
    val visitors: Flow<Set<VisitorNetworkModel>>
    val controlServerState: StateFlow<ControlServerState>


    suspend fun startControl()

    fun restoreVisitors(visitors: Set<VisitorNetworkModel>)
}
