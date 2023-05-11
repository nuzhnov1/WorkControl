package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.api.service.UniversityService
import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class UniversityRemoteDataSource @Inject constructor(
    private val universityService: UniversityService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getBuildingsDTO(): Response<List<BuildingDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            universityService.getBuildings()
        }

    suspend fun getRoomsDTO(buildingID: Long): Response<List<RoomDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            universityService.getBuildingRooms(buildingID)
        }

    suspend fun getFacultiesDTO(): Response<List<FacultyDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            universityService.getFaculties()
        }

    suspend fun getGroupsDTO(facultyID: Long): Response<List<GroupDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            universityService.getFacultyGroups(facultyID)
        }

    suspend fun getStudentsDTO(groupID: Long): Response<List<StudentDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            universityService.getStudentsOfGroup(groupID)
        }
}
