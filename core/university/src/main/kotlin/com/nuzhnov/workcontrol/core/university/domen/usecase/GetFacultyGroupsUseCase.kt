package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.UniversityData
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Faculty
import javax.inject.Inject

class GetFacultyGroupsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(
        faculty: Faculty,
        isRefresh: Boolean
    ): Result<UniversityData<List<Group>>> = repository.getFacultyGroups(faculty, isRefresh)
}
