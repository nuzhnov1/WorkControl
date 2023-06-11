package com.nuzhnov.workcontrol.core.university.data.repository

import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityRemoteDataSource
import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityLocalDataSource
import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.data.api.dto.university.*
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.database.entity.*
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.model.*
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UniversityRepositoryImpl @Inject constructor(
    private val universityRemoteDataSource: UniversityRemoteDataSource,
    private val universityLocalDataSource: UniversityLocalDataSource,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : UniversityRepository {

    override fun getBuildingsFlow(): Flow<LoadResult<List<Building>>> =
        universityLocalDataSource
            .getBuildingEntitiesFlow()
            .map { buildingEntityList ->
                if (buildingEntityList.isEmpty()) {
                    loadBuildings()
                } else {
                    LoadResult.Success(
                        data = buildingEntityList.map(BuildingEntity::toBuilding)
                    )
                }
            }
            .flowOn(context = coroutineDispatcher)

    override fun getBuildingRoomsFlow(building: Building): Flow<LoadResult<List<Room>>> =
        universityLocalDataSource
            .getRoomEntitiesFlow(buildingID = building.id)
            .map { roomEntityList ->
                if (roomEntityList.isEmpty()) {
                    loadBuildingsRooms(building)
                } else {
                    LoadResult.Success(
                        data = roomEntityList.map { roomEntity -> roomEntity.toRoom(building) }
                    )
                }
            }
            .flowOn(context = coroutineDispatcher)

    override fun getDepartmentsFlow(): Flow<LoadResult<List<Department>>> =
        universityLocalDataSource
            .getDepartmentEntitiesFlow()
            .map { departmentEntityList ->
                if (departmentEntityList.isEmpty()) {
                    loadDepartments()
                } else {
                    LoadResult.Success(
                        data = departmentEntityList.map(DepartmentEntity::toDepartment)
                    )
                }
            }
            .flowOn(context = coroutineDispatcher)

    override fun getDepartmentGroupsFlow(department: Department): Flow<LoadResult<List<Group>>> =
        universityLocalDataSource
            .getGroupEntitiesFlow(departmentID = department.id)
            .map { groupEntityList ->
                if (groupEntityList.isEmpty()) {
                    loadDepartmentGroups(department)
                } else {
                    LoadResult.Success(
                        data = groupEntityList.map { groupEntity ->
                            groupEntity.toGroup(department)
                        }
                    )
                }
            }
            .flowOn(context = coroutineDispatcher)

    override fun getStudentsOfGroupFlow(group: Group): Flow<LoadResult<List<Student>>> =
        universityLocalDataSource
            .getStudentEntitiesFlow(groupID = group.id)
            .map { studentEntityList ->
                if (studentEntityList.isEmpty()) {
                    loadStudentsOfGroup(group)
                } else {
                    LoadResult.Success(
                        data = studentEntityList.map { studentEntity ->
                            studentEntity.toStudent(group)
                        }
                    )
                }
            }
            .flowOn(context = coroutineDispatcher)

    override suspend fun loadBuildings(): LoadResult<List<Building>> =
        safeExecute(context = coroutineDispatcher) {
            val response = universityRemoteDataSource.getBuildingsDTO()

            if (response is Response.Success) {
                val buildingDTOList = response.value
                val buildingEntityArray = buildingDTOList
                    .map(BuildingDTO::toBuildingEntity)
                    .toTypedArray()

                universityLocalDataSource
                    .saveBuildingEntities(*buildingEntityArray)
                    .getOrThrow()
            }

            response.toLoadResult { buildingDTOList ->
                buildingDTOList.map(BuildingDTO::toBuilding)
            }
        }.unwrap()

    override suspend fun loadBuildingsRooms(building: Building): LoadResult<List<Room>> =
        safeExecute(context = coroutineDispatcher) {
            val buildingID = building.id
            val response = universityRemoteDataSource.getRoomsDTO(buildingID)

            if (response is Response.Success) {
                val roomDTOList = response.value
                val roomEntityArray = roomDTOList
                    .map { roomDTO -> roomDTO.toRoomEntity(buildingID) }
                    .toTypedArray()

                universityLocalDataSource
                    .saveRoomEntities(*roomEntityArray)
                    .getOrThrow()
            }

            response.toLoadResult { roomDTOList ->
                roomDTOList.map { roomDTO -> roomDTO.toRoom(building) }
            }
        }.unwrap()

    override suspend fun loadDepartments(): LoadResult<List<Department>> =
        safeExecute(context = coroutineDispatcher) {
            val response = universityRemoteDataSource.getDepartmentsDTO()

            if (response is Response.Success) {
                val departmentDTOList = response.value
                val departmentEntityArray = departmentDTOList
                    .map(DepartmentDTO::toDepartmentEntity)
                    .toTypedArray()

                universityLocalDataSource
                    .saveDepartmentEntities(*departmentEntityArray)
                    .getOrThrow()
            }

            response.toLoadResult { departmentDTOList ->
                departmentDTOList.map(DepartmentDTO::toDepartment)
            }
        }.unwrap()

    override suspend fun loadDepartmentGroups(department: Department): LoadResult<List<Group>> =
        safeExecute(context = coroutineDispatcher) {
            val departmentID = department.id
            val response = universityRemoteDataSource.getGroupsDTO(departmentID)

            if (response is Response.Success) {
                val groupDTOList = response.value
                val groupEntityArray = groupDTOList
                    .map { groupDTO -> groupDTO.toGroupEntity(departmentID) }
                    .toTypedArray()

                universityLocalDataSource
                    .saveGroupEntities(*groupEntityArray)
                    .getOrThrow()
            }

            response.toLoadResult { groupDTOList ->
                groupDTOList.map { groupDTO -> groupDTO.toGroup(department) }
            }
        }.unwrap()

    override suspend fun loadStudentsOfGroup(group: Group): LoadResult<List<Student>> =
        safeExecute(context = coroutineDispatcher) {
            val groupID = group.id
            val response = universityRemoteDataSource.getStudentsDTO(groupID)

            if (response is Response.Success) {
                val studentDTOList = response.value
                val studentEntityArray = studentDTOList
                    .map { studentDTO -> studentDTO.toStudentEntity(groupID) }
                    .toTypedArray()

                universityLocalDataSource
                    .saveStudentEntities(*studentEntityArray)
                    .getOrThrow()
            }

            response.toLoadResult { studentDTOList ->
                studentDTOList.map { studentDTO -> studentDTO.toStudent(group) }
            }
        }.unwrap()
}
