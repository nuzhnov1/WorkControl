package com.nuzhnov.workcontrol.core.university.data.repository

import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityRemoteDataSource
import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityLocalDataSource
import com.nuzhnov.workcontrol.core.university.data.mapper.*
import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.UniversityData
import com.nuzhnov.workcontrol.core.university.domen.model.FetchStatus
import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.model.*
import javax.inject.Inject

internal class UniversityRepositoryImpl @Inject constructor(
    private val universityRemoteDataSource: UniversityRemoteDataSource,
    private val universityLocalDataSource: UniversityLocalDataSource
) : UniversityRepository {

    private var cachedBuildings: List<Building>? = null
    private val cachedRooms: MutableMap<Long, List<Room>> = mutableMapOf()
    private var cachedFaculties: List<Faculty>? = null
    private val cachedGroups: MutableMap<Long, List<Group>> = mutableMapOf()
    private val cachedStudents: MutableMap<Long, List<Student>> = mutableMapOf()


    override suspend fun getBuildings(
        isRefresh: Boolean
    ): Result<UniversityData<List<Building>>> = runCatching {
        val buildings = cachedBuildings

        if (buildings == null || isRefresh) {
            when (val response = universityRemoteDataSource.getBuildingsDTO()) {
                is Response.Success<List<BuildingDTO>> -> {
                    val buildingDTOList = response.value

                    val buildingList = buildingDTOList
                        .map { buildingDTO -> buildingDTO.toBuilding() }

                    val buildingEntityArray = buildingList
                        .map { building -> building.toBuildingEntity() }
                        .toTypedArray()

                    cachedBuildings = buildingList
                    universityLocalDataSource.saveBuildingEntities(*buildingEntityArray)

                    UniversityData(buildingList, FetchStatus.Success)
                }

                is Response.Failure -> {
                    val buildingEntityList = universityLocalDataSource.getBuildingEntities()
                    val buildingList = buildingEntityList
                        .map { buildingEntity -> buildingEntity.toBuilding() }

                    UniversityData(buildingList, response.toFailureFetchStatus())
                }
            }
        } else {
            UniversityData(buildings, FetchStatus.Success)
        }
    }

    override suspend fun getBuildingRooms(
        building: Building,
        isRefresh: Boolean
    ): Result<UniversityData<List<Room>>> = runCatching {
        val buildingID = building.id
        val rooms = cachedRooms[buildingID]

        if (rooms == null || isRefresh) {
            when (val response = universityRemoteDataSource.getRoomsDTO(buildingID)) {
                is Response.Success<List<RoomDTO>> -> {
                    val roomDTOList = response.value
                    val roomList = roomDTOList.map { roomDTO -> roomDTO.toRoom(building) }

                    val roomEntityArray = roomList
                        .map { room -> room.toRoomEntity() }
                        .toTypedArray()

                    cachedRooms[buildingID] = roomList
                    universityLocalDataSource.saveRoomEntities(*roomEntityArray)

                    UniversityData(roomList, FetchStatus.Success)
                }

                is Response.Failure -> {
                    val roomEntityList = universityLocalDataSource.getRoomEntities(buildingID)
                    val roomList = roomEntityList
                        .map { roomEntity -> roomEntity.toRoom(building) }

                    UniversityData(roomList, response.toFailureFetchStatus())
                }
            }
        } else {
            UniversityData(rooms, FetchStatus.Success)
        }
    }

    override suspend fun getFaculties(
        isRefresh: Boolean
    ): Result<UniversityData<List<Faculty>>> = runCatching {
        val faculties = cachedFaculties

        if (faculties == null || isRefresh) {
            when (val response = universityRemoteDataSource.getFacultiesDTO()) {
                is Response.Success<List<FacultyDTO>> -> {
                    val facultyDTOList = response.value

                    val facultyList = facultyDTOList
                        .map { facultyDTO -> facultyDTO.toFaculty() }

                    val facultyEntityArray = facultyList
                        .map { faculty -> faculty.toFacultyEntity() }
                        .toTypedArray()

                    cachedFaculties = facultyList
                    universityLocalDataSource.saveFacultyEntities(*facultyEntityArray)

                    UniversityData(facultyList, FetchStatus.Success)
                }

                is Response.Failure -> {
                    val facultyEntityList = universityLocalDataSource.getFacultyEntities()
                    val facultyList = facultyEntityList
                        .map { facultyEntity -> facultyEntity.toFaculty() }

                    UniversityData(facultyList, response.toFailureFetchStatus())
                }
            }
        } else {
            UniversityData(faculties, FetchStatus.Success)
        }
    }

    override suspend fun getFacultyGroups(
        faculty: Faculty,
        isRefresh: Boolean
    ): Result<UniversityData<List<Group>>> = runCatching {
        val facultyID = faculty.id
        val groups = cachedGroups[facultyID]

        if (groups == null || isRefresh) {
            when (val response = universityRemoteDataSource.getGroupsDTO(facultyID)) {
                is Response.Success<List<GroupDTO>> -> {
                    val groupDTOList = response.value
                    val groupList = groupDTOList.map { groupDTO -> groupDTO.toGroup(faculty) }

                    val groupEntityArray = groupList
                        .map { group -> group.toGroupEntity() }
                        .toTypedArray()

                    cachedGroups[facultyID] = groupList
                    universityLocalDataSource.saveGroupEntities(*groupEntityArray)

                    UniversityData(groupList, FetchStatus.Success)
                }

                is Response.Failure -> {
                    val groupEntityList = universityLocalDataSource.getGroupEntities(facultyID)
                    val groupList = groupEntityList
                        .map { groupEntity -> groupEntity.toGroup(faculty) }

                    UniversityData(groupList, response.toFailureFetchStatus())
                }
            }
        } else {
            UniversityData(groups, FetchStatus.Success)
        }
    }

    override suspend fun getStudentsOfGroup(
        group: Group,
        isRefresh: Boolean
    ): Result<UniversityData<List<Student>>> = runCatching {
        val groupID = group.id
        val students = cachedStudents[groupID]

        if (students == null || isRefresh) {
            when (val response = universityRemoteDataSource.getStudentsDTO(groupID)) {
                is Response.Success<List<StudentDTO>> -> {
                    val studentDTOList = response.value

                    val studentList = studentDTOList
                        .map { studentDTO -> studentDTO.toStudent(group) }

                    val studentEntityArray = studentList
                        .map { student -> student.toStudentEntity() }
                        .toTypedArray()

                    cachedStudents[groupID] = studentList
                    universityLocalDataSource.saveStudentEntities(*studentEntityArray)

                    UniversityData(studentList, FetchStatus.Success)
                }

                is Response.Failure -> {
                    val studentEntityList = universityLocalDataSource.getStudentEntities(groupID)
                    val studentList = studentEntityList
                        .map { studentEntity -> studentEntity.toStudent(group) }

                    UniversityData(studentList, response.toFailureFetchStatus())
                }
            }
        } else {
            UniversityData(students, FetchStatus.Success)
        }
    }
}
