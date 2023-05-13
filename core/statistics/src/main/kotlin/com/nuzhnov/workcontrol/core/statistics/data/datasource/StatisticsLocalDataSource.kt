package com.nuzhnov.workcontrol.core.statistics.data.datasource

import com.nuzhnov.workcontrol.core.model.Statistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class StatisticsLocalDataSource @Inject constructor() {

    private val universityStatistics = MutableStateFlow<Statistics?>(value = null)
    private val facultyStatisticsMap = mutableMapOf<Long, MutableStateFlow<Statistics?>>()
    private val groupStatisticsMap = mutableMapOf<Long, MutableStateFlow<Statistics?>>()
    private val studentStatisticsMap = mutableMapOf<Long, MutableStateFlow<Statistics?>>()
    private val currentStudentStatistics = MutableStateFlow<Statistics?>(value = null)


    fun getUniversityStatisticsFlow(): Flow<Statistics?> = universityStatistics

    fun getFacultyStatisticsFlow(facultyID: Long): Flow<Statistics?> =
        facultyStatisticsMap.getOrPut(facultyID) { MutableStateFlow(value = null) }

    fun getGroupStatisticsFlow(groupID: Long): Flow<Statistics?> =
        groupStatisticsMap.getOrPut(groupID) { MutableStateFlow(value = null) }

    fun getStudentStatisticsFlow(studentID: Long): Flow<Statistics?> =
        groupStatisticsMap.getOrPut(studentID) { MutableStateFlow(value = null) }

    fun getCurrentStudentStatisticsFlow(): Flow<Statistics?> = currentStudentStatistics

    fun saveUniversityStatistics(statistics: Statistics) {
        universityStatistics.value = statistics
    }

    fun saveFacultyStatistics(facultyID: Long, statistics: Statistics) {
        val facultyStatisticsFlow = facultyStatisticsMap[facultyID]

        if (facultyStatisticsFlow != null) {
            facultyStatisticsFlow.value = statistics
        } else {
            facultyStatisticsMap[facultyID] = MutableStateFlow(value = statistics)
        }
    }

    fun saveGroupStatistics(groupID: Long, statistics: Statistics) {
        val groupStatisticsFlow = groupStatisticsMap[groupID]

        if (groupStatisticsFlow != null) {
            groupStatisticsFlow.value = statistics
        } else {
            groupStatisticsMap[groupID] = MutableStateFlow(value = statistics)
        }
    }

    fun saveStudentStatistics(studentID: Long, statistics: Statistics) {
        val studentsStatisticsFlow = studentStatisticsMap[studentID]

        if (studentsStatisticsFlow != null) {
            studentsStatisticsFlow.value = statistics
        } else {
            studentStatisticsMap[studentID] = MutableStateFlow(value = statistics)
        }
    }

    fun saveCurrentStudentStatistics(statistics: Statistics) {
        currentStudentStatistics.value = statistics
    }
}
