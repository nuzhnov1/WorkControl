package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFacultiesFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke(): Flow<LoadResult<List<Faculty>>> = repository.getFacultiesFlow()
}
