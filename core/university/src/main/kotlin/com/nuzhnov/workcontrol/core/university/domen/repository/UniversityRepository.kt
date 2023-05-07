package com.nuzhnov.workcontrol.core.university.domen.repository

import com.nuzhnov.workcontrol.core.university.domen.model.UniversityData
import com.nuzhnov.workcontrol.core.model.*

internal interface UniversityRepository {
    suspend fun getBuildings(
        isRefresh: Boolean
    ): Result<UniversityData<List<Building>>>

    suspend fun getBuildingRooms(
        building: Building,
        isRefresh: Boolean
    ): Result<UniversityData<List<Room>>>

    suspend fun getFaculties(
        isRefresh: Boolean
    ): Result<UniversityData<List<Faculty>>>

    suspend fun getFacultyGroups(
        faculty: Faculty,
        isRefresh: Boolean
    ): Result<UniversityData<List<Group>>>

    suspend fun getStudentsOfGroup(
        group: Group,
        isRefresh: Boolean
    ): Result<UniversityData<List<Student>>>
}
