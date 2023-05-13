package com.nuzhnov.workcontrol.core.statistics.domen.usecase

import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUniversityStatisticsFlowUseCase @Inject internal constructor(
    private val repository: StatisticsRepository
) {

    operator fun invoke(): Flow<LoadResult<Statistics>> = repository.getUniversityStatisticsFlow()
}
