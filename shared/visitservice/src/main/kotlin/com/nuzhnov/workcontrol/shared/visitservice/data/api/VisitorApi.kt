package com.nuzhnov.workcontrol.shared.visitservice.data.api

import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.core.visitcontrol.visitor.VisitorState
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitorApi {
    val visitorState: StateFlow<VisitorState>

    suspend fun startVisit(serverAddress: InetAddress, serverPort: Int, visitorID: VisitorID)
}
