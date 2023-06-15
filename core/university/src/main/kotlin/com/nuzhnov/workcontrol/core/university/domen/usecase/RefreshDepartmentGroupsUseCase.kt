package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.models.Department
import javax.inject.Inject

class RefreshDepartmentGroupsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(department: Department) = repository.refreshDepartmentGroups(department)
}
