package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadFacultiesUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    suspend operator fun invoke(): LoadResult<List<Faculty>> = repository.loadFaculties()
}
