package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import javax.inject.Inject

class GetBuildingsFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke() = repository.getBuildingsFlow()
}
