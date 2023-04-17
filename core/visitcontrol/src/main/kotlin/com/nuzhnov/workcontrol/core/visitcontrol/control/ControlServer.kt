package com.nuzhnov.workcontrol.core.visitcontrol.control

import com.nuzhnov.workcontrol.core.visitcontrol.model.Visit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface ControlServer {
    val visits: Flow<Set<Visit>>
    val state: StateFlow<ControlServerState>


    suspend fun start(
        address: InetAddress = defaultAddress,
        port: Int = DEFAULT_PORT,
        backlog: Int = DEFAULT_BACKLOG,
        maxAcceptConnectionAttempts: Int = DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS
    )

    fun setVisits(visits: Set<Visit>)

    fun clearVisits()


    companion object {
        const val DEFAULT_PORT = 0
        const val DEFAULT_BACKLOG = 512
        const val DEFAULT_MAX_ACCEPT_CONNECTION_ATTEMPTS = 16

        val defaultAddress: InetAddress = InetAddress.getLocalHost()


        fun getDefaultControlServer(): ControlServer = ControlServerImpl()
    }
}
