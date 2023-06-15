package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import javax.inject.Inject

class RefreshUserDataUseCase @Inject internal constructor(
    private val repository: SessionRepository
) {

    suspend operator fun invoke() = repository.refreshUserData()
}
