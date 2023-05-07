package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.UniversityData
import com.nuzhnov.workcontrol.core.model.Room
import com.nuzhnov.workcontrol.core.model.Building
import javax.inject.Inject

class GetBuildingRoomsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(
        building: Building,
        isRefresh: Boolean
    ): Result<UniversityData<List<Room>>> = repository.getBuildingRooms(building, isRefresh)
}
