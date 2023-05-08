package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.LoadStatus
import com.nuzhnov.workcontrol.core.model.Faculty
import javax.inject.Inject

class LoadFacultyGroupsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(faculty: Faculty): LoadStatus =
        repository.loadFacultyGroups(faculty)
}
