package com.nuzhnov.workcontrol.core.university.domen.repository

import com.nuzhnov.workcontrol.core.models.*
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import kotlinx.coroutines.flow.Flow

internal interface UniversityRepository {
    fun getBuildingsFlow(): Flow<List<Building>>

    fun getBuildingRoomsFlow(building: Building): Flow<List<Room>>

    fun getDepartmentsFlow(): Flow<List<Department>>

    fun getDepartmentGroupsFlow(department: Department): Flow<List<Group>>

    fun getStudentsOfGroupFlow(group: Group): Flow<List<Student>>

    suspend fun refreshBuildings(): LoadResult

    suspend fun refreshBuildingsRooms(building: Building): LoadResult

    suspend fun refreshDepartments(): LoadResult

    suspend fun refreshDepartmentGroups(department: Department): LoadResult

    suspend fun refreshStudentsOfGroup(group: Group): LoadResult
}
