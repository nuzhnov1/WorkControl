package com.nuzhnov.workcontrol.core.statistics.domen.usecase

import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import javax.inject.Inject

class LoadStudentStatisticsUseCase @Inject internal constructor(
    private val repository: StatisticsRepository
) {

    suspend operator fun invoke(
        student: Student
    ): LoadResult<Statistics> = repository.loadStudentStatistics(student)
}
