package com.nuzhnov.workcontrol.core.visitcontrol.visitor

import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface Visitor {
    val state: StateFlow<VisitorState>

    suspend fun start(serverAddress: InetAddress, serverPort: Int, visitorID: VisitorID)

    companion object {
        fun getDefaultVisitor(): Visitor = VisitorImpl()
    }
}
