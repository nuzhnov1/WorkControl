package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import javax.inject.Inject

class GetUserDataFlowUseCase @Inject internal constructor(
    private val sessionRepository: SessionRepository
) {

    operator fun invoke() = sessionRepository.getUserDataFlow()
}
