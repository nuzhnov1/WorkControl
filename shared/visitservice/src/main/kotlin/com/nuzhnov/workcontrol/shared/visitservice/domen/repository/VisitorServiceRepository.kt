package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress
import android.net.nsd.NsdServiceInfo

internal interface VisitorServiceRepository {
    val servicesFlow: Flow<Map<String, NsdServiceInfo>>
    val serviceState: StateFlow<VisitorServiceState>


    fun getDiscoveredServices(): Map<String, NsdServiceInfo>

    fun updateServiceState(state: VisitorServiceState)

    fun addDiscoveredService(service: NsdServiceInfo)

    fun removeDiscoveredService(service: NsdServiceInfo)

    fun clearDiscoveredServices()

    suspend fun startVisit(serverAddress: InetAddress, serverPort: Int, visitorID: VisitorID)
}
