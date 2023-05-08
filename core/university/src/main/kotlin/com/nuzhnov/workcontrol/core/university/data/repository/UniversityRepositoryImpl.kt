package com.nuzhnov.workcontrol.core.university.data.repository

import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityRemoteDataSource
import com.nuzhnov.workcontrol.core.university.data.datasource.UniversityLocalDataSource
import com.nuzhnov.workcontrol.core.university.data.mapper.*
import com.nuzhnov.workcontrol.core.university.domen.repository.UniversityRepository
import com.nuzhnov.workcontrol.core.university.domen.model.LoadStatus
import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.database.util.safeTransactionExecute
import com.nuzhnov.workcontrol.core.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UniversityRepositoryImpl @Inject constructor(
    private val universityRemoteDataSource: UniversityRemoteDataSource,
    private val universityLocalDataSource: UniversityLocalDataSource
) : UniversityRepository {

    private val cachedBuildings = MutableStateFlow<List<Building>?>(value = null)
    private val cachedRooms = mutableMapOf<Long, MutableStateFlow<List<Room>?>>()
    private val cachedFaculties = MutableStateFlow<List<Faculty>?>(value = null)
    private val cachedGroups = mutableMapOf<Long, MutableStateFlow<List<Group>?>>()
    private val cachedStudents = mutableMapOf<Long, MutableStateFlow<List<Student>?>>()


    override fun getBuildingsFlow(): Flow<List<Building>> = cachedBuildings.map { buildings ->
        buildings ?: safeGetTransaction(defaultValue = listOf()) {
            universityLocalDataSource
                .getBuildingEntities()
                .map { buildingEntity -> buildingEntity.toBuilding() }
        }
    }

    override fun getBuildingRoomsFlow(building: Building): Flow<List<Room>> {
        val buildingID = building.id
        val roomsFlow = cachedRooms[buildingID] ?: MutableStateFlow<List<Room>?>(value = null)

        cachedRooms[buildingID] = roomsFlow

        return roomsFlow.map { rooms ->
            rooms ?: safeGetTransaction(defaultValue = listOf()) {
                universityLocalDataSource
                    .getRoomEntities(buildingID)
                    .map { roomEntity -> roomEntity.toRoom(building) }
            }
        }
    }

    override fun getFacultiesFlow(): Flow<List<Faculty>> = cachedFaculties.map { faculties ->
        faculties ?: safeGetTransaction(defaultValue = listOf()) {
            universityLocalDataSource
                .getFacultyEntities()
                .map { facultyEntity -> facultyEntity.toFaculty() }
        }
    }

    override fun getFacultyGroupsFlow(faculty: Faculty): Flow<List<Group>> {
        val facultyID = faculty.id
        val groupsFlow = cachedGroups[facultyID] ?: MutableStateFlow<List<Group>?>(value = null)

        cachedGroups[facultyID] = groupsFlow

        return groupsFlow.map { groups ->
            groups ?: safeGetTransaction(defaultValue = listOf()) {
                universityLocalDataSource
                    .getGroupEntities(facultyID)
                    .map { groupEntity -> groupEntity.toGroup(faculty) }
            }
        }
    }

    override fun getStudentsOfGroupFlow(group: Group): Flow<List<Student>> {
        val groupID = group.id
        val studentsFlow = cachedStudents[groupID] ?: MutableStateFlow<List<Student>?>(value = null)

        cachedStudents[groupID] = studentsFlow

        return studentsFlow.map { students ->
            students ?: safeGetTransaction(defaultValue = listOf()) {
                universityLocalDataSource
                    .getStudentEntities(groupID)
                    .map { studentEntity -> studentEntity.toStudent(group) }
            }
        }
    }

    override suspend fun loadBuildings(): LoadStatus {
        val response = universityRemoteDataSource.getBuildingsDTO()

        if (response is Response.Success<List<BuildingDTO>>) {
            val buildingDTOList = response.value
            val buildingList = buildingDTOList.map { buildingDTO -> buildingDTO.toBuilding() }

            val buildingEntityArray = buildingList
                .map { building -> building.toBuildingEntity() }
                .toTypedArray()

            cachedBuildings.value = buildingList

            safeTransactionExecute {
                universityLocalDataSource.saveBuildingEntities(*buildingEntityArray)
            }
        }

        return response.toLoadStatus()
    }

    override suspend fun loadBuildingsRooms(building: Building): LoadStatus {
        val buildingID = building.id
        val roomsFlow = cachedRooms[buildingID] ?: MutableStateFlow<List<Room>?>(value = null)
        val response = universityRemoteDataSource.getRoomsDTO(buildingID)

        if (response is Response.Success<List<RoomDTO>>) {
            val roomDTOList = response.value
            val roomList = roomDTOList.map { roomDTO -> roomDTO.toRoom(building) }
            val roomEntityList = roomList.map { room -> room.toRoomEntity() }.toTypedArray()

            roomsFlow.value = roomList
            cachedRooms[buildingID] = roomsFlow

            safeTransactionExecute {
                universityLocalDataSource.saveRoomEntities(*roomEntityList)
            }
        }

        return response.toLoadStatus()
    }

    override suspend fun loadFaculties(): LoadStatus {
        val response = universityRemoteDataSource.getFacultiesDTO()

        if (response is Response.Success<List<FacultyDTO>>) {
            val facultyDTOList = response.value
            val facultyList = facultyDTOList.map { facultyDTO -> facultyDTO.toFaculty() }

            val facultyEntityArray = facultyList
                .map { faculty -> faculty.toFacultyEntity() }
                .toTypedArray()

            cachedFaculties.value = facultyList

            safeTransactionExecute {
                universityLocalDataSource.saveFacultyEntities(*facultyEntityArray)
            }
        }

        return response.toLoadStatus()
    }

    override suspend fun loadFacultyGroups(faculty: Faculty): LoadStatus {
        val facultyID = faculty.id
        val groupsFlow = cachedGroups[facultyID] ?: MutableStateFlow<List<Group>?>(value = null)
        val response = universityRemoteDataSource.getGroupsDTO(facultyID)

        if (response is Response.Success<List<GroupDTO>>) {
            val groupDTOList = response.value
            val groupList = groupDTOList.map { groupDTO -> groupDTO.toGroup(faculty) }
            val groupEntityList = groupList.map { group -> group.toGroupEntity() }.toTypedArray()

            groupsFlow.value = groupList
            cachedGroups[facultyID] = groupsFlow

            safeTransactionExecute {
                universityLocalDataSource.saveGroupEntities(*groupEntityList)
            }
        }

        return response.toLoadStatus()
    }

    override suspend fun loadStudentsOfGroup(group: Group): LoadStatus {
        val groupID = group.id
        val studentsFlow = cachedStudents[groupID] ?: MutableStateFlow<List<Student>?>(value = null)
        val response = universityRemoteDataSource.getStudentsDTO(groupID)

        if (response is Response.Success<List<StudentDTO>>) {
            val studentDTOList = response.value
            val studentList = studentDTOList.map { studentDTO -> studentDTO.toStudent(group) }

            val studentEntityArray = studentList
                .map { student -> student.toStudentEntity() }
                .toTypedArray()

            studentsFlow.value = studentList
            cachedStudents[groupID] = studentsFlow

            safeTransactionExecute {
                universityLocalDataSource.saveStudentEntities(*studentEntityArray)
            }
        }

        return response.toLoadStatus()
    }

    private suspend inline fun <T> safeGetTransaction(
        defaultValue: T,
        crossinline transaction: suspend () -> T
    ): T = safeTransactionExecute(transaction).getOrDefault(defaultValue)
}
