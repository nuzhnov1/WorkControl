package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Room
import com.nuzhnov.workcontrol.core.model.Building
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadBuildingRoomsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(building: Building): LoadResult<List<Room>> =
        repository.loadBuildingsRooms(building)
}
