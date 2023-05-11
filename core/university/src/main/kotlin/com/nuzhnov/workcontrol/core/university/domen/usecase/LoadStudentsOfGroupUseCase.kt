package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadStudentsOfGroupUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(group: Group): LoadResult<List<Student>> =
        repository.loadStudentsOfGroup(group)
}
