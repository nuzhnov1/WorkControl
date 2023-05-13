package com.nuzhnov.workcontrol.core.statistics.data.datasource

import com.nuzhnov.workcontrol.core.data.api.dto.statistics.StatisticsDTO
import com.nuzhnov.workcontrol.core.data.api.service.StatisticsService
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher

internal class StatisticsRemoteDataSource(
    private val statisticsService: StatisticsService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getUniversityStatistics(): Response<StatisticsDTO> =
        safeApiCall(context = coroutineDispatcher) {
            statisticsService.getUniversityStatistics()
        }

    suspend fun getFacultyStatistics(facultyID: Long): Response<StatisticsDTO> =
        safeApiCall(context = coroutineDispatcher) {
            statisticsService.getFacultyStatistics(facultyID)
        }

    suspend fun getGroupStatistics(groupID: Long): Response<StatisticsDTO> =
        safeApiCall(context = coroutineDispatcher) {
            statisticsService.getGroupStatistics(groupID)
        }

    suspend fun getStudentStatistics(studentID: Long): Response<StatisticsDTO> =
        safeApiCall(context = coroutineDispatcher) {
            statisticsService.getStudentStatistics(studentID)
        }

    suspend fun getCurrentStudentStatistics(): Response<StatisticsDTO> =
        safeApiCall(context = coroutineDispatcher) {
            statisticsService.getStudentStatistics()
        }
}
