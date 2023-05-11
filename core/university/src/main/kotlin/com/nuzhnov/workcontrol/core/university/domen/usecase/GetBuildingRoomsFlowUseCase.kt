package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Room
import com.nuzhnov.workcontrol.core.model.Building
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBuildingRoomsFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke(building: Building): Flow<List<Room>> =
        repository.getBuildingRoomsFlow(building)
}