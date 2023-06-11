package com.nuzhnov.workcontrol.core.university.domen.repository

import com.nuzhnov.workcontrol.core.model.*
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow

internal interface UniversityRepository {
    fun getBuildingsFlow(): Flow<LoadResult<List<Building>>>

    fun getBuildingRoomsFlow(building: Building): Flow<LoadResult<List<Room>>>

    fun getDepartmentsFlow(): Flow<LoadResult<List<Department>>>

    fun getDepartmentGroupsFlow(department: Department): Flow<LoadResult<List<Group>>>

    fun getStudentsOfGroupFlow(group: Group): Flow<LoadResult<List<Student>>>

    suspend fun loadBuildings(): LoadResult<List<Building>>

    suspend fun loadBuildingsRooms(building: Building): LoadResult<List<Room>>

    suspend fun loadDepartments(): LoadResult<List<Department>>

    suspend fun loadDepartmentGroups(department: Department): LoadResult<List<Group>>

    suspend fun loadStudentsOfGroup(group: Group): LoadResult<List<Student>>
}
