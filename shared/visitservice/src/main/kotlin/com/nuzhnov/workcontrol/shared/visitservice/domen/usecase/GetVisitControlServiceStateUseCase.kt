package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitControlServiceRepository
import javax.inject.Inject

class GetVisitControlServiceStateUseCase @Inject constructor() {
    @Inject internal lateinit var repository: VisitControlServiceRepository

    operator fun invoke() = repository.serviceState
}
