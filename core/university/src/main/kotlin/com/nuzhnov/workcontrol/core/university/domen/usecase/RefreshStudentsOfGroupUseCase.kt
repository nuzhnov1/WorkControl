package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.models.Group
import javax.inject.Inject

class RefreshStudentsOfGroupUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(group: Group) = repository.refreshStudentsOfGroup(group)
}