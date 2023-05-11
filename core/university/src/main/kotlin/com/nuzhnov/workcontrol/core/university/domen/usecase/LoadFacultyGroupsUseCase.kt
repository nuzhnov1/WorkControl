package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadFacultyGroupsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(faculty: Faculty): LoadResult<List<Group>> =
        repository.loadFacultyGroups(faculty)
}
