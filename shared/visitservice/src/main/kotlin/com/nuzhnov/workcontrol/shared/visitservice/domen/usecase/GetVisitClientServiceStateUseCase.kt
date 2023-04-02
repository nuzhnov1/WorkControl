package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitClientServiceRepository
import javax.inject.Inject

class GetVisitClientServiceStateUseCase @Inject constructor() {
    @Inject internal lateinit var repository: VisitClientServiceRepository

    operator fun invoke() = repository.serviceState
}
