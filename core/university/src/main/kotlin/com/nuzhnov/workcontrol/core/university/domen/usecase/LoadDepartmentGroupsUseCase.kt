package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Department
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadDepartmentGroupsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(department: Department): LoadResult<List<Group>> =
        repository.loadDepartmentGroups(department)
}
