package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitorServiceRepository {
    val discoveredServices: Flow<Set<String>>
    val serviceState: StateFlow<VisitorServiceState>


    fun updateServiceState(state: VisitorServiceState)

    fun addDiscoveredService(discoveredService: String)

    fun removeDiscoveredService(discoveredService: String)

    fun clearDiscoveredServices()

    suspend fun startVisit(serverAddress: InetAddress, serverPort: Int, visitorID: VisitorID)
}
