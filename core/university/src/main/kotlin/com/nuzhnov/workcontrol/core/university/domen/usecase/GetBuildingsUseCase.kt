package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.UniversityData
import com.nuzhnov.workcontrol.core.model.Building
import javax.inject.Inject

class GetBuildingsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(isRefresh: Boolean): Result<UniversityData<List<Building>>> =
        repository.getBuildings(isRefresh)
}
