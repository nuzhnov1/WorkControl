package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import javax.inject.Inject

class LoginUseCase @Inject internal constructor(
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke(
        login: String,
        password: String,
        isLoginSave: Boolean
    ) = sessionRepository.login(login, password, isLoginSave)
}
