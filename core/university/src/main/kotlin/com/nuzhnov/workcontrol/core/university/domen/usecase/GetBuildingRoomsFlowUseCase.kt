package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.models.Building
import javax.inject.Inject

class GetBuildingRoomsFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke(building: Building) = repository.getBuildingRoomsFlow(building)
}
