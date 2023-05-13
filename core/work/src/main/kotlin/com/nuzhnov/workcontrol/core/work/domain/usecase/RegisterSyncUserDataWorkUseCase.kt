package com.nuzhnov.workcontrol.core.work.domain.usecase

import com.nuzhnov.workcontrol.core.work.domain.repository.WorkRepository
import javax.inject.Inject

class RegisterSyncUserDataWorkUseCase @Inject internal constructor(
    private val repository: WorkRepository
) {

    operator fun invoke(): Unit = repository.registerSyncUserDataWork()
}
