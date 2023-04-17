package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

internal class DiscoveredServicesLocalDataSource @Inject constructor() {
    private val _services = MutableStateFlow<Map<String, NsdServiceInfo>>(value = mapOf())
    val servicesFlow: Flow<Map<String, NsdServiceInfo>> = _services


    fun getServices() = _services.value

    fun addService(service: NsdServiceInfo) {
        _services.applyUpdate { put(service.serviceName, service) }
    }

    fun removeDiscoveredService(service: NsdServiceInfo) {
        _services.applyUpdate { remove(service.serviceName) }
    }

    fun clearDiscoveredServices() {
        _services.applyUpdate { clear() }
    }

    private fun MutableStateFlow<Map<String, NsdServiceInfo>>.applyUpdate(
        block: MutableMap<String, NsdServiceInfo>.() -> Unit
    ) {
        value = value.toMutableMap().apply(block)
    }
}
