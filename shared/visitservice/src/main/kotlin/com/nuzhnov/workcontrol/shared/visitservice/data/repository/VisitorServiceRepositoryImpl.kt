package com.nuzhnov.workcontrol.shared.visitservice.data.repository

import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.VisitorServiceRemoteDataSource
import com.nuzhnov.workcontrol.shared.visitservice.data.datasource.DiscoveredServicesLocalDataSource
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import java.net.InetAddress
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

internal class VisitorServiceRepositoryImpl @Inject constructor(
    private val visitorServiceRemoteDataSource: VisitorServiceRemoteDataSource,
    private val discoveredServicesLocalDataSource: DiscoveredServicesLocalDataSource
) : VisitorServiceRepository {

    override val servicesFlow = discoveredServicesLocalDataSource.servicesFlow
    override val serviceState = visitorServiceRemoteDataSource.serviceState


    override fun getDiscoveredServices(): Map<String, NsdServiceInfo> {
        return discoveredServicesLocalDataSource.getServices()
    }

    override fun updateServiceState(state: VisitorServiceState) {
        visitorServiceRemoteDataSource.updateServiceState(state)
    }

    override fun addDiscoveredService(service: NsdServiceInfo) {
        discoveredServicesLocalDataSource.addService(service)
    }

    override fun removeDiscoveredService(service: NsdServiceInfo) {
        discoveredServicesLocalDataSource.removeDiscoveredService(service)
    }

    override fun clearDiscoveredServices() {
        discoveredServicesLocalDataSource.clearDiscoveredServices()
    }

    override suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        visitorID: VisitorID
    ) = visitorServiceRemoteDataSource.startVisit(serverAddress, serverPort, visitorID)
}
