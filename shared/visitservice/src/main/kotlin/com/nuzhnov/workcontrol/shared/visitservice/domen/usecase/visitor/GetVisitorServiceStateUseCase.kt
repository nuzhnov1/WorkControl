package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import javax.inject.Inject

class GetVisitorServiceStateUseCase @Inject internal constructor(
    private val repository: VisitorServiceRepository
) {
    operator fun invoke() = repository.serviceState
}
