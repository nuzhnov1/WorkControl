package com.nuzhnov.workcontrol.core.university.data.repository

import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityRemoteDataSource
import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityLocalDataSource
import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.database.entity.*
import com.nuzhnov.workcontrol.core.mapper.*
import com.nuzhnov.workcontrol.core.model.*
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class UniversityRepositoryImpl @Inject constructor(
    private val universityRemoteDataSource: UniversityRemoteDataSource,
    private val universityLocalDataSource: UniversityLocalDataSource
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

    override fun getFacultiesFlow(): Flow<LoadResult<List<Faculty>>> =
        universityLocalDataSource
            .getFacultyEntitiesFlow()
            .map { facultyEntityList ->
                if (facultyEntityList.isEmpty()) {
                    loadFaculties()
                } else {
                    LoadResult.Success(
                        data = facultyEntityList.map(FacultyEntity::toFaculty)
                    )
                }
            }

    override fun getFacultyGroupsFlow(faculty: Faculty): Flow<LoadResult<List<Group>>> =
        universityLocalDataSource
            .getGroupEntitiesFlow(facultyID = faculty.id)
            .map { groupEntityList ->
                if (groupEntityList.isEmpty()) {
                    loadFacultyGroups(faculty)
                } else {
                    LoadResult.Success(
                        data = groupEntityList.map { groupEntity -> groupEntity.toGroup(faculty) }
                    )
                }
            }

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

    override suspend fun loadBuildings(): LoadResult<List<Building>> = safeExecute {
        val response = universityRemoteDataSource.getBuildingsDTO()

        if (response is Response.Success<List<BuildingDTO>>) {
            val buildingDTOList = response.value
            val buildingEntityArray = buildingDTOList
                .map(BuildingDTO::toBuildingEntity)
                .toTypedArray()

            universityLocalDataSource
                .saveBuildingEntities(*buildingEntityArray)
                .getOrThrow()
        }

        response.toLoadResult { buildingDTOList -> buildingDTOList.map(BuildingDTO::toBuilding) }
    }.unwrap()

    override suspend fun loadBuildingsRooms(
        building: Building
    ): LoadResult<List<Room>> = safeExecute {
        val buildingID = building.id
        val response = universityRemoteDataSource.getRoomsDTO(buildingID)

        if (response is Response.Success<List<RoomDTO>>) {
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

    override suspend fun loadFaculties(): LoadResult<List<Faculty>> = safeExecute {
        val response = universityRemoteDataSource.getFacultiesDTO()

        if (response is Response.Success<List<FacultyDTO>>) {
            val facultyDTOList = response.value
            val facultyEntityArray = facultyDTOList
                .map(FacultyDTO::toFacultyEntity)
                .toTypedArray()

            universityLocalDataSource
                .saveFacultyEntities(*facultyEntityArray)
                .getOrThrow()
        }

        response.toLoadResult { facultyDTOList -> facultyDTOList.map(FacultyDTO::toFaculty) }
    }.unwrap()

    override suspend fun loadFacultyGroups(
        faculty: Faculty
    ): LoadResult<List<Group>> = safeExecute {
        val facultyID = faculty.id
        val response = universityRemoteDataSource.getGroupsDTO(facultyID)

        if (response is Response.Success<List<GroupDTO>>) {
            val groupDTOList = response.value
            val groupEntityArray = groupDTOList
                .map { groupDTO -> groupDTO.toGroupEntity(facultyID) }
                .toTypedArray()

            universityLocalDataSource
                .saveGroupEntities(*groupEntityArray)
                .getOrThrow()
        }

        response.toLoadResult { groupDTOList ->
            groupDTOList.map { groupDTO -> groupDTO.toGroup(faculty) }
        }
    }.unwrap()

    override suspend fun loadStudentsOfGroup(
        group: Group
    ): LoadResult<List<Student>> = safeExecute {
        val groupID = group.id
        val response = universityRemoteDataSource.getStudentsDTO(groupID)

        if (response is Response.Success<List<StudentDTO>>) {
            val studentDTOList = response.value
            val studentEntityArray = studentDTOList
                .map { studentDTO -> studentDTO.toStudentEntity(groupID) }
                .toTypedArray()

            universityLocalDataSource.saveStudentEntities(*studentEntityArray)
        }

        response.toLoadResult { studentDTOList ->
            studentDTOList.map { studentDTO -> studentDTO.toStudent(group) }
        }
    }.unwrap()
}
