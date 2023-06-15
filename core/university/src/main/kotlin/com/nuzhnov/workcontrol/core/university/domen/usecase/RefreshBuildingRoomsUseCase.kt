package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.models.Building
import javax.inject.Inject

class RefreshBuildingRoomsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(building: Building) = repository.refreshBuildingsRooms(building)
}
