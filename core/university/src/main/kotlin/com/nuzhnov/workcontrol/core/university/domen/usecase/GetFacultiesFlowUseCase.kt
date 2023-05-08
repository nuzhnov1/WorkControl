package com.nuzhnov.workcontrol.core.university.domen.usecase

import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.model.Faculty
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFacultiesFlowUseCase @Inject internal constructor(
    private val repository: UniversityRepository
) {

    operator fun invoke(): Flow<List<Faculty>> = repository.getFacultiesFlow()
}
