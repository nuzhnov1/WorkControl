package com.nuzhnov.workcontrol.core.visitservice.studentservice.data.datasource

import com.nuzhnov.workcontrol.common.util.applyUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

internal class NsdDiscoveredServicesLocalDataSource @Inject constructor() {
    private val _nsdServicesFlow = MutableStateFlow<Map<String, NsdServiceInfo>>(value = mapOf())
    val nsdServicesFlow: Flow<Map<String, NsdServiceInfo>> = _nsdServicesFlow


    fun getNsdServices(): Map<String, NsdServiceInfo> = _nsdServicesFlow.value

    fun addService(service: NsdServiceInfo): Unit = _nsdServicesFlow.applyUpdate {
        put(service.serviceName, service)
    }

    fun removeDiscoveredService(service: NsdServiceInfo): Unit = _nsdServicesFlow.applyUpdate {
        remove(service.serviceName)
    }

    fun clearDiscoveredServices(): Unit = _nsdServicesFlow.applyUpdate { clear() }
}
