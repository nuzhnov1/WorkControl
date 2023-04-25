package com.nuzhnov.workcontrol.shared.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.shared.studentservice.domen.repository.StudentServiceRepository
import javax.inject.Inject

internal class ClearNsdDiscoveredServicesUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke() {
        repository.clearNsdDiscoveredServices()
    }
}
