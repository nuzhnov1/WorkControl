package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetControlServiceStateUseCase @Inject internal constructor(
    private val repository: ControlServiceRepository
) {
    operator fun invoke(): StateFlow<ControlServiceState> = repository.serviceState
}
