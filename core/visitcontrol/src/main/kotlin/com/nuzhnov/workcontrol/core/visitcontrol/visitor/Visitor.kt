package com.nuzhnov.workcontrol.core.visitcontrol.visitor

import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface Visitor {
    val state: StateFlow<VisitorState>

    suspend fun start(serverAddress: InetAddress, serverPort: Int, visitorID: Long)

    companion object {
        fun getDefaultVisitor(): Visitor = VisitorImpl()
    }
}
