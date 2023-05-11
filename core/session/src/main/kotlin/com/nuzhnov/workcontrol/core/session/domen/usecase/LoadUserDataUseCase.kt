package com.nuzhnov.workcontrol.core.session.domen.usecase

import com.nuzhnov.workcontrol.core.session.domen.repository.SessionRepository
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadUserDataUseCase @Inject internal constructor(
    private val repository: SessionRepository
) {

    suspend operator fun invoke(): LoadResult<UserData> = repository.loadUserData()
}
