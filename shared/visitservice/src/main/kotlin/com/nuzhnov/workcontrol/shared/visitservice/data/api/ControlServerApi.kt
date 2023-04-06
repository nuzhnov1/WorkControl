package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServer
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface ControlServerApi {
    val visits: Flow<Set<Visit>>
    val controlServerState: StateFlow<ControlServerState>

    suspend fun startControl(
        address: InetAddress = CONTROL_SERVER_DEFAULT_ADDRESS,
        port: Int = CONTROL_SERVER_DEFAULT_PORT
    )

    fun restoreVisits(visits: Set<Visit>)

    companion object {
        val CONTROL_SERVER_DEFAULT_ADDRESS = ControlServer.DEFAULT_ADDRESS
        const val CONTROL_SERVER_DEFAULT_PORT = ControlServer.DEFAULT_PORT
    }
}
