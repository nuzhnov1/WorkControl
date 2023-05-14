package com.nuzhnov.workcontrol.core.statistics.data.repository

import com.nuzhnov.workcontrol.core.statistics.data.datasource.StatisticsRemoteDataSource
import com.nuzhnov.workcontrol.core.statistics.data.datasource.StatisticsLocalDataSource
import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import com.nuzhnov.workcontrol.core.data.mapper.toLoadResult
import com.nuzhnov.workcontrol.core.data.mapper.toStatistics
import com.nuzhnov.workcontrol.core.data.mapper.unwrap
import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class StatisticsRepositoryImpl @Inject constructor(
    private val statisticsRemoteDataSource: StatisticsRemoteDataSource,
    private val statisticsLocalDataSource: StatisticsLocalDataSource,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
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
            .flowOn(context = coroutineDispatcher)

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
            .flowOn(context = coroutineDispatcher)

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
            .flowOn(context = coroutineDispatcher)

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
            .flowOn(context = coroutineDispatcher)

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
            .flowOn(context = coroutineDispatcher)

    override suspend fun loadUniversityStatistics(): LoadResult<Statistics> =
        safeExecute(context = coroutineDispatcher) {
            statisticsRemoteDataSource
                .getUniversityStatistics()
                .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
                .onSuccess { statistics ->
                    statisticsLocalDataSource.saveUniversityStatistics(statistics = statistics)
                }
        }.unwrap()

    override suspend fun loadFacultyStatistics(faculty: Faculty): LoadResult<Statistics> =
        safeExecute(context = coroutineDispatcher) {
            statisticsRemoteDataSource
                .getFacultyStatistics(facultyID = faculty.id)
                .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
                .onSuccess { statistics ->
                    statisticsLocalDataSource.saveFacultyStatistics(
                        facultyID = faculty.id,
                        statistics = statistics
                    )
                }
        }.unwrap()

    override suspend fun loadGroupStatistics(group: Group): LoadResult<Statistics> =
        safeExecute(context = coroutineDispatcher) {
            statisticsRemoteDataSource
                .getGroupStatistics(groupID = group.id)
                .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
                .onSuccess { statistics ->
                    statisticsLocalDataSource.saveGroupStatistics(
                        groupID = group.id,
                        statistics = statistics
                    )
                }
        }.unwrap()

    override suspend fun loadStudentStatistics(student: Student): LoadResult<Statistics> =
        safeExecute(context = coroutineDispatcher) {
            statisticsRemoteDataSource
                .getStudentStatistics(studentID = student.id)
                .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
                .onSuccess { statistics ->
                    statisticsLocalDataSource.saveStudentStatistics(
                        studentID = student.id,
                        statistics = statistics
                    )
                }
        }.unwrap()

    override suspend fun loadCurrentStudentStatistics(): LoadResult<Statistics> =
        safeExecute(context = coroutineDispatcher) {
            statisticsRemoteDataSource
                .getCurrentStudentStatistics()
                .toLoadResult { statisticsDTO -> statisticsDTO.toStatistics() }
                .onSuccess { statistics ->
                    statisticsLocalDataSource.saveCurrentStudentStatistics(
                        statistics = statistics
                    )
                }
        }.unwrap()

    private inline fun <T> LoadResult<T>.onSuccess(
        block: (T) -> Unit
    ): LoadResult<T> = apply {
        when (this) {
            is LoadResult.Success -> block(data)
            is LoadResult.Failure -> Unit
        }
    }
}
