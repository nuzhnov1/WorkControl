package com.nuzhnov.workcontrol.core.teacherservice.data.api

import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ControlServerApiImpl @Inject constructor(
    private val controlServer: ControlServer
) : ControlServerApi {

    override val visitsFlow: Flow<Set<Visit>> = controlServer.visits
    override val serverState = controlServer.state


    override suspend fun startServer() {
        controlServer.start()
    }

    override fun disconnectVisitor(visitorID: VisitorID) = controlServer.run {
        disconnectVisitor(visitorID)
        removeVisits(visitorID)
    }

    override fun restoreVisits(visitsArray: Array<Visit>) {
        controlServer.updateVisits(*visitsArray)
    }

    override fun clearVisits() {
        controlServer.clearVisits()
    }
}
