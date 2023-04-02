package com.nuzhnov.workcontrol.core.visitcontrol.server

import com.nuzhnov.workcontrol.core.visitcontrol.model.Visitor
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface Server {
    val visitors: StateFlow<Set<Visitor>>
    val state: StateFlow<ServerState>

    suspend fun start(
        address: InetAddress = DEFAULT_ADDRESS,
        port: Int = DEFAULT_PORT,
        backlog: Int = DEFAULT_BACKLOG,
        maxAcceptConnectionAttempts: Int = DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS
    )

    fun clearVisitors()

    companion object {
        val DEFAULT_ADDRESS: InetAddress = InetAddress.getLocalHost()
        const val DEFAULT_PORT = 0
        const val DEFAULT_BACKLOG = 512
        const val DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS = 16

        fun getDefaultServer(): Server = ServerImpl()
    }
}
