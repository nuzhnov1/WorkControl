package com.nuzhnov.workcontrol.shared.studentservice.data.api

import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.Visitor
import java.net.InetAddress
import javax.inject.Inject

internal class VisitorApiImpl @Inject constructor(private val visitor: Visitor) : VisitorApi {
    override val visitorState = visitor.state

    override suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) {
        visitor.startVisit(serverAddress, serverPort, visitorID)
    }
}
