package com.nuzhnov.workcontrol.core.statistics.domen.usecase

import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStudentStatisticsFlowUseCase @Inject internal constructor(
    private val repository: StatisticsRepository
) {

    operator fun invoke(
        student: Student
    ): Flow<LoadResult<Statistics>> = repository.getStudentStatisticsFlow(student)
}
