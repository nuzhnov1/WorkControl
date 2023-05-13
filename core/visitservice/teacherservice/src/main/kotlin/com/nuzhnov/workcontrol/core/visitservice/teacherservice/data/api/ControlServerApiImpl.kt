package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.api

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


    override suspend fun startServer(): Unit = controlServer.start()

    override fun disconnectVisitor(visitorID: VisitorID): Unit = controlServer.run {
        disconnectVisitor(visitorID)
        removeVisits(visitorID)
    }

    override fun restoreVisits(vararg visit: Visit): Unit = controlServer.updateVisits(*visit)

    override fun clearVisits(): Unit = controlServer.clearVisits()
}
