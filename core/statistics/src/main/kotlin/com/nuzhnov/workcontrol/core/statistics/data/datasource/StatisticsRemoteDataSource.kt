package com.nuzhnov.workcontrol.core.statistics.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.StatisticsService
import com.nuzhnov.workcontrol.core.data.api.dto.statistics.StatisticsDTO
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import javax.inject.Inject

internal class StatisticsRemoteDataSource @Inject constructor(
    private val statisticsService: StatisticsService
) {

    suspend fun getUniversityStatistics(): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getUniversityStatistics() }

    suspend fun getDepartmentStatistics(departmentID: Long): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getDepartmentStatistics(departmentID) }

    suspend fun getGroupStatistics(groupID: Long): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getGroupStatistics(groupID) }

    suspend fun getStudentStatistics(studentID: Long): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getStudentStatistics(studentID) }

    suspend fun getCurrentStudentStatistics(): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getStudentStatistics() }
}
