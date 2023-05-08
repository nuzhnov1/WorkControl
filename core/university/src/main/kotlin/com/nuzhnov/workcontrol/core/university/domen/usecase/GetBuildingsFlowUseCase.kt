package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Building
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBuildingsFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke(): Flow<List<Building>> = repository.getBuildingsFlow()
}
