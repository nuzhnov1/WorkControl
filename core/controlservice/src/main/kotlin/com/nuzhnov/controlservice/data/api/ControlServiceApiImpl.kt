package com.nuzhnov.controlservice.data.api

import com.nuzhnov.controlservice.data.api.client.IControlClient
import com.nuzhnov.controlservice.data.api.server.IControlServer

class ControlServiceApiImpl : ControlServiceApi {
    override val server: IControlServer get() = IControlServer.getDefaultControlServer()
    override val client: IControlClient get() = IControlClient.getDefaultControlClient()
}
