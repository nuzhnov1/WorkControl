package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.DiscoveredService
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress
import android.net.nsd.NsdServiceInfo


internal interface StudentServiceRepository {
    val discoveredServicesFlow: Flow<Set<DiscoveredService>>
    val serviceState: StateFlow<StudentServiceState>


    fun getDiscoveredServices(): Set<DiscoveredService>

    fun getNsdDiscoveredServicesMap(): Map<String, NsdServiceInfo>

    fun updateServiceState(state: StudentServiceState)

    fun updateStudentID(id: Long?)

    fun addNsdDiscoveredService(nsdService: NsdServiceInfo)

    fun removeNsdDiscoveredService(nsdService: NsdServiceInfo)

    fun clearNsdDiscoveredServices()

    suspend fun startVisit(serverAddress: InetAddress, serverPort: Int)
}
