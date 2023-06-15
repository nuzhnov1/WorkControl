package com.nuzhnov.workcontrol.core.statistics.data.repository

import com.nuzhnov.workcontrol.core.statistics.data.datasource.StatisticsRemoteDataSource
import com.nuzhnov.workcontrol.core.statistics.data.datasource.StatisticsLocalDataSource
import com.nuzhnov.workcontrol.core.statistics.domen.repository.StatisticsRepository
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.preferences.model.Session
import com.nuzhnov.workcontrol.core.data.mapper.toDepartmentStatisticsEntity
import com.nuzhnov.workcontrol.core.data.mapper.toGroupStatisticsEntity
import com.nuzhnov.workcontrol.core.data.mapper.toLoadResult
import com.nuzhnov.workcontrol.core.data.mapper.toStatistics
import com.nuzhnov.workcontrol.core.data.mapper.toStudentStatisticsEntity
import com.nuzhnov.workcontrol.core.data.mapper.toUniversityStatisticsEntity
import com.nuzhnov.workcontrol.core.data.mapper.unwrap
import com.nuzhnov.workcontrol.core.models.Department
import com.nuzhnov.workcontrol.core.models.Group
import com.nuzhnov.workcontrol.core.models.Role
import com.nuzhnov.workcontrol.core.models.Student
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.util.roles.requireRole
import com.nuzhnov.workcontrol.core.util.roles.requireTeacherRole
import com.nuzhnov.workcontrol.core.util.roles.requireStudentRole
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class StatisticsRepositoryImpl @Inject constructor(
    private val statisticsRemoteDataSource: StatisticsRemoteDataSource,
    private val statisticsLocalDataSource: StatisticsLocalDataSource,
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : StatisticsRepository {

    override fun getUniversityStatisticsFlow() = flow {
        appPreferences.requireTeacherRole()

        val flow = statisticsLocalDataSource
            .getUniversityStatisticsFlow()
            .map { entity -> entity?.toStatistics() }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getDepartmentStatisticsFlow(department: Department) = flow {
        appPreferences.requireTeacherRole()

        val flow = statisticsLocalDataSource
            .getDepartmentStatisticsFlow(departmentID = department.id)
            .map { entity -> entity?.toStatistics() }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getGroupStatisticsFlow(group: Group) = flow {
        appPreferences.requireTeacherRole()

        val flow = statisticsLocalDataSource
            .getGroupStatisticsFlow(groupID = group.id)
            .map { entity -> entity?.toStatistics() }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getStudentStatisticsFlow(student: Student) = flow {
        appPreferences.requireTeacherRole()

        val flow = statisticsLocalDataSource
            .getStudentStatisticsFlow(studentID = student.id)
            .map { entity -> entity?.toStatistics() }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getCurrentStudentStatisticsFlow() = flow {
        val studentID = appPreferences.requireStudentRole().id

        val flow = statisticsLocalDataSource
            .getStudentStatisticsFlow(studentID)
            .map { entity -> entity?.toStatistics() }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override suspend fun refreshUniversityStatistics() =
        refresh(role = Role.TEACHER) {
            val response = statisticsRemoteDataSource.getUniversityStatistics()

            if (response is Response.Success) {
                val statisticsEntity = response.value.toUniversityStatisticsEntity()
                statisticsLocalDataSource.saveUniversityStatistics(statisticsEntity)
            }

            response.toLoadResult()
        }

    override suspend fun refreshDepartmentStatistics(department: Department) =
        refresh(role = Role.TEACHER) {
            val response = statisticsRemoteDataSource.getDepartmentStatistics(department.id)

            if (response is Response.Success) {
                val statisticsEntity = response.value.toDepartmentStatisticsEntity(department.id)
                statisticsLocalDataSource.saveDepartmentStatistics(statisticsEntity)
            }

            response.toLoadResult()
        }

    override suspend fun refreshGroupStatistics(group: Group) =
        refresh(role = Role.TEACHER) {
            val response = statisticsRemoteDataSource.getGroupStatistics(group.id)

            if (response is Response.Success) {
                val statisticsEntity = response.value.toGroupStatisticsEntity(group.id)
                statisticsLocalDataSource.saveGroupStatistics(statisticsEntity)
            }

            response.toLoadResult()
        }

    override suspend fun refreshStudentStatistics(student: Student) =
        refresh(role = Role.TEACHER) {
            val response = statisticsRemoteDataSource.getStudentStatistics(student.id)

            if (response is Response.Success) {
                val statisticsEntity = response.value.toStudentStatisticsEntity(student.id)
                statisticsLocalDataSource.saveStudentStatistics(statisticsEntity)
            }

            response.toLoadResult()
        }

    override suspend fun refreshCurrentStudentStatistics() =
        refresh(role = Role.STUDENT) { session ->
            val response = statisticsRemoteDataSource.getCurrentStudentStatistics()

            if (response is Response.Success) {
                val statisticsEntity = response.value.toStudentStatisticsEntity(studentID = session.id)
                statisticsLocalDataSource.saveStudentStatistics(statisticsEntity)
            }

            response.toLoadResult()
        }

    private suspend fun refresh(role: Role, block: suspend (Session) -> LoadResult) =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.requireRole(role)
            safeExecute { block(session) }.unwrap()
        }
}
