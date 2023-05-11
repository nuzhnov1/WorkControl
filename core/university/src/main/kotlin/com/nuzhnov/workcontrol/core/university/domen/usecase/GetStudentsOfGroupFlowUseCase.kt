package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Group
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStudentsOfGroupFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke(group: Group): Flow<List<Student>> =
        repository.getStudentsOfGroupFlow(group)
}