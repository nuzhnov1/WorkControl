package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import javax.inject.Inject

class GetSavedLoginFlowUseCase @Inject internal constructor(
    private val repository: SessionRepository
) {

    operator fun invoke() = repository.getSavedLoginFlow()
}
