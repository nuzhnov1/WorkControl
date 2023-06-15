package com.nuzhnov.workcontrol.core.works.domain.usecase

import com.nuzhnov.workcontrol.core.works.domain.repository.WorkRepository
import javax.inject.Inject

class RegisterSyncLocalDataWorkUseCase @Inject internal constructor(
    private val repository: WorkRepository
) {

    operator fun invoke() = repository.registerSyncLocalDataWork()
}
