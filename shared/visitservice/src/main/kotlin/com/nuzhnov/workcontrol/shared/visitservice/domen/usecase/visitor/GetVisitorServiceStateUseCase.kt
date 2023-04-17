package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetVisitorServiceStateUseCase @Inject internal constructor(
    private val repository: VisitorServiceRepository
) {
    operator fun invoke(): StateFlow<VisitorServiceState> = repository.serviceState
}
