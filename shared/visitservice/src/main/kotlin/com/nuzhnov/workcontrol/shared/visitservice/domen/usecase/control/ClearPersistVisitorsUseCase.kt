package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import javax.inject.Inject

class ClearPersistVisitorsUseCase @Inject internal constructor(
    private val repository: ControlServiceRepository
) {
    suspend operator fun invoke() = repository.clearPersistVisitors()
}
