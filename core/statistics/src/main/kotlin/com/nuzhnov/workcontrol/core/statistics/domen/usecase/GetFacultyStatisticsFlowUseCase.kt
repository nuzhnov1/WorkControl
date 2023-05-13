package com.nuzhnov.workcontrol.core.statistics.domen.usecase

import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFacultyStatisticsFlowUseCase @Inject internal constructor(
    private val repository: StatisticsRepository
) {

    operator fun invoke(
        faculty: Faculty
    ): Flow<LoadResult<Statistics>> = repository.getFacultyStatisticsFlow(faculty)
}
