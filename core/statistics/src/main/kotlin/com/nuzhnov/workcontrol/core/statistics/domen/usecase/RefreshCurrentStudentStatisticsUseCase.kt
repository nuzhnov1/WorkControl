package com.nuzhnov.workcontrol.core.statistics.domen.usecase

import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import javax.inject.Inject

class RefreshCurrentStudentStatisticsUseCase @Inject internal constructor(
    private val repository: StatisticsRepository
) {

    suspend operator fun invoke() = repository.refreshCurrentStudentStatistics()
}
