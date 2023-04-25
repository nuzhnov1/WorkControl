package com.nuzhnov.workcontrol.shared.studentservice.domen.usecase

import com.nuzhnov.workcontrol.shared.studentservice.domen.repository.StudentServiceRepository
import com.nuzhnov.workcontrol.shared.studentservice.domen.model.DiscoveredService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscoveredServicesFlowUseCase @Inject internal constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(): Flow<Set<DiscoveredService>> = repository.discoveredServicesFlow
}
