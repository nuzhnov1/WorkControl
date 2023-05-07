package com.nuzhnov.workcontrol.core.visitservice.studentservice.data.mapper

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.DiscoveredService
import android.net.nsd.NsdServiceInfo


internal fun Map<String, NsdServiceInfo>.toDiscoveredServicesSet(): Set<DiscoveredService> =
    values.map { nsdServiceInfo -> nsdServiceInfo.toDiscoveredService() }.toSet()

private fun NsdServiceInfo.toDiscoveredService(): DiscoveredService = DiscoveredService(
    name = serviceName,
    host = host,
    port = port
)
