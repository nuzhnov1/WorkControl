package com.nuzhnov.workcontrol.core.statistics.domen.repository

import com.nuzhnov.workcontrol.core.models.Statistics
import com.nuzhnov.workcontrol.core.models.Department
import com.nuzhnov.workcontrol.core.models.Group
import com.nuzhnov.workcontrol.core.models.Student
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import kotlinx.coroutines.flow.Flow

internal interface StatisticsRepository {
    fun getUniversityStatisticsFlow(): Flow<Statistics?>

    fun getDepartmentStatisticsFlow(department: Department): Flow<Statistics?>

    fun getGroupStatisticsFlow(group: Group): Flow<Statistics?>

    fun getStudentStatisticsFlow(student: Student): Flow<Statistics?>

    fun getCurrentStudentStatisticsFlow(): Flow<Statistics?>

    suspend fun refreshUniversityStatistics(): LoadResult

    suspend fun refreshDepartmentStatistics(department: Department): LoadResult

    suspend fun refreshGroupStatistics(group: Group): LoadResult

    suspend fun refreshStudentStatistics(student: Student): LoadResult

    suspend fun refreshCurrentStudentStatistics(): LoadResult
}
