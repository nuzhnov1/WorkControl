package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository.StudentServiceRepository
import javax.inject.Inject

internal class ClearNsdDiscoveredServicesUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(): Unit = repository.clearNsdDiscoveredServices()
}
