package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.models.Department
import javax.inject.Inject

class GetDepartmentGroupsFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke(department: Department) = repository.getDepartmentGroupsFlow(department)
}
