package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.LoadStatus
import javax.inject.Inject

class LoadFacultiesUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(): LoadStatus = repository.loadFaculties()
}
