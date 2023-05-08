package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.LoadStatus
import com.nuzhnov.workcontrol.core.model.Building
import javax.inject.Inject

class LoadBuildingRoomsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(building: Building): LoadStatus =
        repository.loadBuildingsRooms(building)
}
