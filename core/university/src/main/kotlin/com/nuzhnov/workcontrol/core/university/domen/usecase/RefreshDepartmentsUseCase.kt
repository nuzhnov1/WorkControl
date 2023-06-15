package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import javax.inject.Inject

class RefreshDepartmentsUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke() = repository.refreshDepartments()
}
