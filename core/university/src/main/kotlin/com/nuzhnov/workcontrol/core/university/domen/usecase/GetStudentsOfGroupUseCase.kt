package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.UniversityData
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Group
import javax.inject.Inject

class GetStudentsOfGroupUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(
        group: Group,
        isRefresh: Boolean
    ): Result<UniversityData<List<Student>>> = repository.getStudentsOfGroup(group, isRefresh)
}
