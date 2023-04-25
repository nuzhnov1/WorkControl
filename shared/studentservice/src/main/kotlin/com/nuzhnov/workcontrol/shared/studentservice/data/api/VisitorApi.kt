package com.nuzhnov.workcontrol.shared.studentservice.data.api

import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorState
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitorApi {
    val visitorState: StateFlow<VisitorState>

    suspend fun startVisit(serverAddress: InetAddress, serverPort: Int, visitorID: VisitorID)
}
