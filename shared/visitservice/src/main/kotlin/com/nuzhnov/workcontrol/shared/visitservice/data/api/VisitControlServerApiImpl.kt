package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.server.Server
import java.net.InetAddress
import javax.inject.Inject

internal class VisitControlServerApiImpl @Inject constructor(
    private val server: Server
) : VisitControlServerApi {

    override val visitors = server.visitors
    override val serverState = server.state


    override suspend fun startServer(address: InetAddress, port: Int) {
        server.clearVisitors()
        server.start(address, port)
    }
}
