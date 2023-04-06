package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.core.visitcontrol.visitor.Visitor
import java.net.InetAddress
import javax.inject.Inject

internal class VisitorApiImpl @Inject constructor(
    private val visitor: Visitor
) : VisitorApi {

    override val visitorState = visitor.state


    override suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) {
        visitor.start(serverAddress, serverPort, visitorID)
    }
}
