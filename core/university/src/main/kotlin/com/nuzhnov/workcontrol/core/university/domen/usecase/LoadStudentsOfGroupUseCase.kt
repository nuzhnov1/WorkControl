package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.LoadStatus
import com.nuzhnov.workcontrol.core.model.Group
import javax.inject.Inject

class LoadStudentsOfGroupUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(group: Group): LoadStatus =
        repository.loadStudentsOfGroup(group)
}
