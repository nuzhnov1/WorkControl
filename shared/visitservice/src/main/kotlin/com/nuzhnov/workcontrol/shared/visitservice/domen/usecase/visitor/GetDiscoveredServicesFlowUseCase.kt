package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

class GetDiscoveredServicesFlowUseCase @Inject internal constructor(
    private val repository: VisitorServiceRepository
) {
    operator fun invoke(): Flow<Set<NsdServiceInfo>> = repository
        .servicesFlow
        .map { services -> services.values.toSet() }
}
