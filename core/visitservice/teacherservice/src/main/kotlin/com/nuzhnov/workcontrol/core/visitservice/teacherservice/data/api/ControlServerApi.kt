package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.api

import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface ControlServerApi {
    val visitsFlow: Flow<Set<Visit>>
    val serverState: StateFlow<ControlServerState>


    suspend fun startServer()

    fun disconnectVisitor(visitorID: VisitorID)

    fun restoreVisits(vararg visit: Visit)

    fun clearVisits()
}
