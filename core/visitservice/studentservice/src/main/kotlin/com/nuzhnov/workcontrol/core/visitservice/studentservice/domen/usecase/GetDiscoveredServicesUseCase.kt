package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository.StudentServiceRepository
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.DiscoveredService
import javax.inject.Inject

class GetDiscoveredServicesUseCase @Inject internal constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(): Set<DiscoveredService> = repository.getDiscoveredServices()
}
