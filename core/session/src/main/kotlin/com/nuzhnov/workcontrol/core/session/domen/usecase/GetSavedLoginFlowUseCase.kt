package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedLoginFlowUseCase @Inject internal constructor(
    private val repository: SessionRepository
) {

    operator fun invoke(): Flow<String?> = repository.getSavedLoginFlow()
}
