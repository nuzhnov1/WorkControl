package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.UniversityService
import com.nuzhnov.workcontrol.core.data.api.dto.university.*
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.data.api.util.Response
import javax.inject.Inject

internal class UniversityRemoteDataSource @Inject constructor(
    private val universityService: UniversityService
) {

    suspend fun getBuildingsDTO(): Response<List<BuildingDTO>> =
        safeApiCall { universityService.getBuildings() }

    suspend fun getRoomsDTO(buildingID: Long): Response<List<RoomDTO>> =
        safeApiCall { universityService.getBuildingRooms(buildingID) }

    suspend fun getFacultiesDTO(): Response<List<FacultyDTO>> =
        safeApiCall { universityService.getFaculties() }

    suspend fun getGroupsDTO(facultyID: Long): Response<List<GroupDTO>> =
        safeApiCall { universityService.getFacultyGroups(facultyID) }

    suspend fun getStudentsDTO(groupID: Long): Response<List<StudentDTO>> =
        safeApiCall { universityService.getStudentsOfGroup(groupID) }
}
