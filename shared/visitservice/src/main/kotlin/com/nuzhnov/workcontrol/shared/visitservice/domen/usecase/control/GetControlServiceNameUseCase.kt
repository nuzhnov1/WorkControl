package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetControlServiceNameUseCase @Inject internal constructor(
    private val repository: ControlServiceRepository
) {
    operator fun invoke(): StateFlow<String?> = repository.serviceName
}
