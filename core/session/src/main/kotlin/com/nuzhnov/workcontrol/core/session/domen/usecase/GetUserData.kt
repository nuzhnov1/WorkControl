package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import javax.inject.Inject

class GetUserData @Inject internal constructor(
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke(): Result<UserData?> = sessionRepository.getUserData()
}
