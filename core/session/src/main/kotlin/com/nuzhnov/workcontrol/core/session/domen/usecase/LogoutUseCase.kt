package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import javax.inject.Inject

class LogoutUseCase @Inject internal constructor(
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke(): Result<Unit> = sessionRepository.logout()
}
