package com.nuzhnov.workcontrol.shared.visitservice.data.remote.api

import com.nuzhnov.workcontrol.shared.visitservice.data.remote.util.VisitorNetworkModel
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ControlServerApiImpl @Inject constructor(
    private val controlServer: ControlServer
) : ControlServerApi {

    override val visitorsFlow: Flow<Set<VisitorNetworkModel>> = controlServer.visits
    override val controlServerState = controlServer.state


    override suspend fun startControl() = controlServer.run {
        clearVisits()
        start()
    }

    override fun restoreVisitors(visitors: Set<VisitorNetworkModel>) {
        controlServer.setVisits(visitors)
    }
}
