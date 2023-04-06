package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitorServiceRepository {
    val state: StateFlow<VisitorServiceState>
    val discoveredServices: Flow<Set<String>>

    fun updateState(state: VisitorServiceState)
    fun addDiscoveredService(discoveredService: String)
    fun removeDiscoveredService(discoveredService: String)
    fun clearDiscoveredServices()
    suspend fun startVisit(serverAddress: InetAddress, serverPort: Int, visitorID: VisitorID)
}
