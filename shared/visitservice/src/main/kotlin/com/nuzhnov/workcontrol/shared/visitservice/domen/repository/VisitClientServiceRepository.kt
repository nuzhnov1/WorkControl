package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceState
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitClientServiceRepository {
    val serviceState: StateFlow<VisitClientServiceState>

    fun updateServiceState(serviceState: VisitClientServiceState)
    suspend fun startVisitClient(serverAddress: InetAddress, serverPort: Int, visitorID: Long)
}
