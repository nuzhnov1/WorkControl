package com.nuzhnov.workcontrol.core.visitcontrol

import com.nuzhnov.workcontrol.core.visitcontrol.server.IServer
import com.nuzhnov.workcontrol.core.visitcontrol.client.IClient

internal class VisitControlServiceImpl : VisitControlService() {
    override val controlServer: IServer = IServer.getDefaultControlServer()
    override val client: IClient = IClient.getDefaultControlClient()
}
