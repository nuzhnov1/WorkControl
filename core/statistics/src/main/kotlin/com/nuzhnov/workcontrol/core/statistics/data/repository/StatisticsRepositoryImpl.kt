package com.nuzhnov.workcontrol.core.statistics.data.repository

import com.nuzhnov.workcontrol.core.statistics.data.datasource.StatisticsRemoteDataSource
import com.nuzhnov.workcontrol.core.statistics.data.datasource.StatisticsLocalDataSource
import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import com.nuzhnov.workcontrol.core.mapper.toLoadResult
import com.nuzhnov.workcontrol.core.mapper.toStatistics
import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class StatisticsRepositoryImpl @Inject constructor(
    private val statisticsRemoteDataSource: StatisticsRemoteDataSource,
    private val statisticsLocalDataSource: StatisticsLocalDataSource
) : StatisticsRepository {

    override fun getUniversityStatisticsFlow(): Flow<LoadResult<Statistics>> =
        statisticsLocalDataSource
            .getUniversityStatisticsFlow()
            .map { statistics ->
                if (statistics == null) {
                    loadUniversityStatistics()
                } else {
                    LoadResult.Success(data = statistics)
                }
            }

    override fun getFacultyStatisticsFlow(faculty: Faculty): Flow<LoadResult<Statistics>> =
        statisticsLocalDataSource
            .getFacultyStatisticsFlow(facultyID = faculty.id)
            .map { statistics ->
                if (statistics == null) {
                    loadFacultyStatistics(faculty)
                } else {
                    LoadResult.Success(data = statistics)
                }
            }

    override fun getGroupStatisticsFlow(group: Group): Flow<LoadResult<Statistics>> =
        statisticsLocalDataSource
            .getGroupStatisticsFlow(groupID = group.id)
            .map { statistics ->
                if (statistics == null) {
                    loadGroupStatistics(group)
                } else {
                    LoadResult.Success(data = statistics)
                }
            }

    override fun getStudentStatisticsFlow(student: Student): Flow<LoadResult<Statistics>> =
        statisticsLocalDataSource
            .getStudentStatisticsFlow(studentID = student.id)
            .map { statistics ->
                if (statistics == null) {
                    loadStudentStatistics(student)
                } else {
                    LoadResult.Success(data = statistics)
                }
            }

    override fun getCurrentStudentStatisticsFlow(): Flow<LoadResult<Statistics>> =
        statisticsLocalDataSource
            .getCurrentStudentStatisticsFlow()
            .map { statistics ->
                if (statistics == null) {
                    loadCurrentStudentStatistics()
                } else {
                    LoadResult.Success(data = statistics)
                }
            }

    override suspend fun loadUniversityStatistics(): LoadResult<Statistics> =
        statisticsRemoteDataSource
            .getUniversityStatistics()
            .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
            .onSuccess { statistics ->
                statisticsLocalDataSource.saveUniversityStatistics(statistics)
            }

    override suspend fun loadFacultyStatistics(faculty: Faculty): LoadResult<Statistics> =
        statisticsRemoteDataSource
            .getFacultyStatistics(facultyID = faculty.id)
            .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
            .onSuccess { statistics ->
                statisticsLocalDataSource.saveFacultyStatistics(facultyID = faculty.id, statistics)
            }

    override suspend fun loadGroupStatistics(group: Group): LoadResult<Statistics> =
        statisticsRemoteDataSource
            .getGroupStatistics(groupID = group.id)
            .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
            .onSuccess { statistics ->
                statisticsLocalDataSource.saveGroupStatistics(groupID = group.id, statistics)
            }

    override suspend fun loadStudentStatistics(student: Student): LoadResult<Statistics> =
        statisticsRemoteDataSource
            .getStudentStatistics(studentID = student.id)
            .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
            .onSuccess { statistics ->
                statisticsLocalDataSource.saveStudentStatistics(studentID = student.id, statistics)
            }

    override suspend fun loadCurrentStudentStatistics(): LoadResult<Statistics> =
        statisticsRemoteDataSource
            .getCurrentStudentStatistics()
            .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
            .onSuccess { statistics ->
                statisticsLocalDataSource.saveCurrentStudentStatistics(statistics)
            }

    private inline fun <T> LoadResult<T>.onSuccess(
        block: (T) -> Unit
    ): LoadResult<T> = apply {
        when (this) {
            is LoadResult.Success -> block(data)
            is LoadResult.Failure -> Unit
        }
    }
}
