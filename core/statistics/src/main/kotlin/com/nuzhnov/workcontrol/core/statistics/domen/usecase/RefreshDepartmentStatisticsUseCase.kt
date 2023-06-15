package com.nuzhnov.workcontrol.core.statistics.domen.usecase

import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import com.nuzhnov.workcontrol.core.models.Department
import javax.inject.Inject

class RefreshDepartmentStatisticsUseCase @Inject internal constructor(
    private val repository: StatisticsRepository
) {

    suspend operator fun invoke(department: Department) = repository.refreshDepartmentStatistics(department)
}
