package com.nuzhnov.workcontrol.core.university.domen.repository

import com.nuzhnov.workcontrol.core.model.*
import com.nuzhnov.workcontrol.core.university.domen.model.LoadStatus
import kotlinx.coroutines.flow.Flow

internal interface UniversityRepository {
    fun getBuildingsFlow(): Flow<List<Building>>

    fun getBuildingRoomsFlow(building: Building): Flow<List<Room>>

    fun getFacultiesFlow(): Flow<List<Faculty>>

    fun getFacultyGroupsFlow(faculty: Faculty): Flow<List<Group>>

    fun getStudentsOfGroupFlow(group: Group): Flow<List<Student>>

    suspend fun loadBuildings(): LoadStatus

    suspend fun loadBuildingsRooms(building: Building): LoadStatus

    suspend fun loadFaculties(): LoadStatus

    suspend fun loadFacultyGroups(faculty: Faculty): LoadStatus

    suspend fun loadStudentsOfGroup(group: Group): LoadStatus
}
