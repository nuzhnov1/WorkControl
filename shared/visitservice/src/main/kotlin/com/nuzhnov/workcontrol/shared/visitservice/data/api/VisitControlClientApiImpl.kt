package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.client.Client
import java.net.InetAddress
import javax.inject.Inject

internal class VisitControlClientApiImpl @Inject constructor(
    private val client: Client,
) : VisitControlClientApi {

    override val clientState = client.state


    override suspend fun startClient(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: Long
    ) {
        client.start(serverAddress, serverPort, visitorID)
    }
}
