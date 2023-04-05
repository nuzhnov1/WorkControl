package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServer
import java.net.InetAddress
import javax.inject.Inject

internal class ControlServerApiImpl @Inject constructor(
    private val controlServer: ControlServer
) : ControlServerApi {

    override val visits = controlServer.visits
    override val controlServerState = controlServer.state


    override suspend fun startControl(address: InetAddress, port: Int) {
        controlServer.clearVisits()
        controlServer.start(address, port)
    }
}
