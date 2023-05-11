package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataFlowUseCase @Inject internal constructor(
    private val sessionRepository: SessionRepository
) {

    operator fun invoke(): Flow<UserData?> = sessionRepository.getUserDataFlow()
}
