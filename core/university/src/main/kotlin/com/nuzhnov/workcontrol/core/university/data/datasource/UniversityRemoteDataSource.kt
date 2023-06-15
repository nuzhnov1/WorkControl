package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.UniversityService
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import javax.inject.Inject

internal class UniversityRemoteDataSource @Inject constructor(
    private val universityService: UniversityService
) {

    suspend fun getBuildingsDTO() = safeApiCall { universityService.getBuildings() }

    suspend fun getRoomsDTO(buildingID: Long) = safeApiCall { universityService.getBuildingRooms(buildingID) }

    suspend fun getDepartmentsDTO() = safeApiCall { universityService.getDepartments() }

    suspend fun getGroupsDTO(departmentID: Long) = safeApiCall { universityService.getDepartmentGroups(departmentID) }

    suspend fun getStudentsDTO(groupID: Long) = safeApiCall { universityService.getStudentsOfGroup(groupID) }
}
