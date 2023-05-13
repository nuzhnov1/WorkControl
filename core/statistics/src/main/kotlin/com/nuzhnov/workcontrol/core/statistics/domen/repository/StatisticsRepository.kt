package com.nuzhnov.workcontrol.core.statistics.domen.repository

import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow

internal interface StatisticsRepository {
    fun getUniversityStatisticsFlow(): Flow<LoadResult<Statistics>>

    fun getFacultyStatisticsFlow(faculty: Faculty): Flow<LoadResult<Statistics>>

    fun getGroupStatisticsFlow(group: Group): Flow<LoadResult<Statistics>>

    fun getStudentStatisticsFlow(student: Student): Flow<LoadResult<Statistics>>

    fun getCurrentStudentStatisticsFlow(): Flow<LoadResult<Statistics>>

    suspend fun loadUniversityStatistics(): LoadResult<Statistics>

    suspend fun loadFacultyStatistics(faculty: Faculty): LoadResult<Statistics>

    suspend fun loadGroupStatistics(group: Group): LoadResult<Statistics>

    suspend fun loadStudentStatistics(student: Student): LoadResult<Statistics>

    suspend fun loadCurrentStudentStatistics(): LoadResult<Statistics>
}
