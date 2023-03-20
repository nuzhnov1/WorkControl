package com.nuzhnov.workcontrol.core.controlservice

import com.nuzhnov.workcontrol.core.controlservice.server.IControlServer
import com.nuzhnov.workcontrol.core.controlservice.client.IControlClient

class ControlServiceApiImpl : ControlServiceApi {
    override val server: IControlServer get() = IControlServer.getDefaultControlServer()
    override val client: IControlClient get() = IControlClient.getDefaultControlClient()
}
