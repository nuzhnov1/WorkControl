package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

class GetDiscoveredServicesUseCase @Inject internal constructor(
    private val repository: VisitorServiceRepository
) {
    operator fun invoke(): Set<NsdServiceInfo> = repository
        .getDiscoveredServices()
        .values
        .toSet()
}