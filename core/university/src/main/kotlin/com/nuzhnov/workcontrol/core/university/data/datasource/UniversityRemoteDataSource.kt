package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.api.service.UniversityService
import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UniversityRemoteDataSource @Inject constructor(
    private val universityService: UniversityService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getBuildingsDTO(): Response<List<BuildingDTO>> =
        withContext(context = coroutineDispatcher) {
            safeApiCall { universityService.getBuildings() }
        }

    suspend fun getRoomsDTO(buildingID: Long): Response<List<RoomDTO>> =
        withContext(context = coroutineDispatcher) {
            safeApiCall { universityService.getBuildingRooms(buildingID) }
        }

    suspend fun getFacultiesDTO(): Response<List<FacultyDTO>> =
        withContext(context = coroutineDispatcher) {
            safeApiCall { universityService.getFaculties() }
        }

    suspend fun getGroupsDTO(facultyID: Long): Response<List<GroupDTO>> =
        withContext(context = coroutineDispatcher) {
            safeApiCall { universityService.getFacultyGroups(facultyID) }
        }

    suspend fun getStudentsDTO(groupID: Long): Response<List<StudentDTO>> =
        withContext(context = coroutineDispatcher) {
            safeApiCall { universityService.getStudentsOfGroup(groupID) }
        }
}
