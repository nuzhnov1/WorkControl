package com.nuzhnov.workcontrol.core.statistics.data.datasource

import com.nuzhnov.workcontrol.core.data.api.dto.statistics.StatisticsDTO
import com.nuzhnov.workcontrol.core.data.api.service.StatisticsService
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall

internal class StatisticsRemoteDataSource(private val statisticsService: StatisticsService) {

    suspend fun getUniversityStatistics(): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getUniversityStatistics() }

    suspend fun getFacultyStatistics(facultyID: Long): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getFacultyStatistics(facultyID) }

    suspend fun getGroupStatistics(groupID: Long): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getGroupStatistics(groupID) }

    suspend fun getStudentStatistics(studentID: Long): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getStudentStatistics(studentID) }

    suspend fun getCurrentStudentStatistics(): Response<StatisticsDTO> =
        safeApiCall { statisticsService.getStudentStatistics() }
}
