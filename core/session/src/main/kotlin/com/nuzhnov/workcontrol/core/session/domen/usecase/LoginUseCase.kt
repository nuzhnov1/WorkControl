package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject internal constructor(
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke(
        login: String,
        password: String
    ): LoginResult = sessionRepository.login(login, password)
}
