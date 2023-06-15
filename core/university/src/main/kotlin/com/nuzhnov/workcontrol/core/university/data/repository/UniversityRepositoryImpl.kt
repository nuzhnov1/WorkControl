package com.nuzhnov.workcontrol.core.university.data.repository

import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityRemoteDataSource
import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityLocalDataSource
import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.data.api.dto.university.*
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.database.entity.*
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.preferences.model.Session
import com.nuzhnov.workcontrol.core.models.*
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.util.roles.requireTeacherRole
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UniversityRepositoryImpl @Inject constructor(
    private val universityRemoteDataSource: UniversityRemoteDataSource,
    private val universityLocalDataSource: UniversityLocalDataSource,
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : UniversityRepository {

    override fun getBuildingsFlow() = flow {
        val flow = appPreferences
            .requireTeacherRole(universityLocalDataSource.getBuildingEntitiesFlow())
            .map { entityList -> entityList.map(BuildingEntity::toBuilding) }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getBuildingRoomsFlow(building: Building) = flow {
        val flow = appPreferences
            .requireTeacherRole(universityLocalDataSource.getRoomsFlow(building.id))
            .map { entityList -> entityList.map { entity -> entity.toRoom(building) } }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getDepartmentsFlow() = flow {
        val flow = appPreferences
            .requireTeacherRole(universityLocalDataSource.getDepartmentEntitiesFlow())
            .map { entityList -> entityList.map(DepartmentEntity::toDepartment) }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getDepartmentGroupsFlow(department: Department) = flow {
        val flow = appPreferences
            .requireTeacherRole(universityLocalDataSource.getGroupEntitiesFlow(department.id))
            .map { entityList -> entityList.map { entity -> entity.toGroup(department) } }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getStudentsOfGroupFlow(group: Group) = flow {
        val flow = appPreferences
            .requireTeacherRole(universityLocalDataSource.getStudentEntitiesFlow(group.id))
            .map { entityList -> entityList.map { entity -> entity.toStudent(group) } }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override suspend fun refreshBuildings() = refresh {
        val response = universityRemoteDataSource.getBuildingsDTO()

        if (response is Response.Success) {
            val entityArray = response.value.map(BuildingDTO::toBuildingEntity).toTypedArray()
            universityLocalDataSource.saveBuildingEntities(*entityArray)
        }

        response.toLoadResult()
    }

    override suspend fun refreshBuildingsRooms(building: Building) = refresh {
        val response = universityRemoteDataSource.getRoomsDTO(building.id)

        if (response is Response.Success) {
            val roomEntities = response.value.map { dto -> dto.toRoomEntity(building.id) }.toTypedArray()
            universityLocalDataSource.saveRoomEntities(*roomEntities)
        }

        response.toLoadResult()
    }

    override suspend fun refreshDepartments() = refresh {
        val response = universityRemoteDataSource.getDepartmentsDTO()

        if (response is Response.Success) {
            val departmentEntities = response.value.map(DepartmentDTO::toDepartmentEntity).toTypedArray()
            universityLocalDataSource.saveDepartmentEntities(*departmentEntities)
        }

        response.toLoadResult()
    }

    override suspend fun refreshDepartmentGroups(department: Department) = refresh {
        val response = universityRemoteDataSource.getGroupsDTO(department.id)

        if (response is Response.Success) {
            val groupEntities = response.value.map { dto -> dto.toGroupEntity(department.id) }.toTypedArray()
            universityLocalDataSource.saveGroupEntities(*groupEntities)
        }

        response.toLoadResult()
    }

    override suspend fun refreshStudentsOfGroup(group: Group) = refresh {
        val response = universityRemoteDataSource.getStudentsDTO(group.id)

        if (response is Response.Success) {
            val studentEntities = response.value.map { dto -> dto.toStudentEntity(group.id) }.toTypedArray()
            universityLocalDataSource.saveStudentEntities(*studentEntities)
        }

        response.toLoadResult()
    }

    private suspend fun refresh(block: suspend (Session) -> LoadResult) =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.requireTeacherRole()
            safeExecute { block(session) }.unwrap()
        }
}
