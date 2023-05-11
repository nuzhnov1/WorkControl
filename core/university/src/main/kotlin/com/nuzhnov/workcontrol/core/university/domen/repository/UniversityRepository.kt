package com.nuzhnov.workcontrol.core.university.domen.repository

import com.nuzhnov.workcontrol.core.model.*
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow

internal interface UniversityRepository {
    fun getBuildingsFlow(): Flow<LoadResult<List<Building>>>

    fun getBuildingRoomsFlow(building: Building): Flow<LoadResult<List<Room>>>

    fun getFacultiesFlow(): Flow<LoadResult<List<Faculty>>>

    fun getFacultyGroupsFlow(faculty: Faculty): Flow<LoadResult<List<Group>>>

    fun getStudentsOfGroupFlow(group: Group): Flow<LoadResult<List<Student>>>

    suspend fun loadBuildings(): LoadResult<List<Building>>

    suspend fun loadBuildingsRooms(building: Building): LoadResult<List<Room>>

    suspend fun loadFaculties(): LoadResult<List<Faculty>>

    suspend fun loadFacultyGroups(faculty: Faculty): LoadResult<List<Group>>

    suspend fun loadStudentsOfGroup(group: Group): LoadResult<List<Student>>
}
