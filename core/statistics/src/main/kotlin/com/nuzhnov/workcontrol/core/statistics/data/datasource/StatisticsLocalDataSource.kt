package com.nuzhnov.workcontrol.core.statistics.data.datasource

import com.nuzhnov.workcontrol.core.data.database.dao.UniversityStatisticsDAO
import com.nuzhnov.workcontrol.core.data.database.dao.DepartmentStatisticsDAO
import com.nuzhnov.workcontrol.core.data.database.dao.GroupStatisticsDAO
import com.nuzhnov.workcontrol.core.data.database.dao.StudentStatisticsDAO
import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentStatisticsEntity
import com.nuzhnov.workcontrol.core.data.database.entity.GroupStatisticsEntity
import com.nuzhnov.workcontrol.core.data.database.entity.StudentStatisticsEntity
import com.nuzhnov.workcontrol.core.data.database.entity.UniversityStatisticsEntity
import javax.inject.Inject

internal class StatisticsLocalDataSource @Inject constructor(
    private val universityStatisticsDAO: UniversityStatisticsDAO,
    private val departmentStatisticsDAO: DepartmentStatisticsDAO,
    private val groupStatisticsDAO: GroupStatisticsDAO,
    private val studentStatisticsDAO: StudentStatisticsDAO
) {

    fun getUniversityStatisticsFlow() = universityStatisticsDAO.getEntityFlow()

    fun getDepartmentStatisticsFlow(departmentID: Long) = departmentStatisticsDAO.getEntityFlow(departmentID)

    fun getGroupStatisticsFlow(groupID: Long) = groupStatisticsDAO.getEntityFlow(groupID)

    fun getStudentStatisticsFlow(studentID: Long) = studentStatisticsDAO.getEntityFlow(studentID)

    suspend fun saveUniversityStatistics(universityStatisticsEntity: UniversityStatisticsEntity) {
        universityStatisticsDAO.insertOrUpdate(universityStatisticsEntity)
    }

    suspend fun saveDepartmentStatistics(departmentStatisticsEntity: DepartmentStatisticsEntity) {
        departmentStatisticsDAO.insertOrUpdate(departmentStatisticsEntity)
    }

    suspend fun saveGroupStatistics(groupStatisticsEntity: GroupStatisticsEntity) {
        groupStatisticsDAO.insertOrUpdate(groupStatisticsEntity)
    }

    suspend fun saveStudentStatistics(studentStatisticsEntity: StudentStatisticsEntity) {
        studentStatisticsDAO.insertOrUpdate(studentStatisticsEntity)
    }
}
