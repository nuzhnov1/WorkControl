package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.database.dao.*
import com.nuzhnov.workcontrol.core.database.entity.*
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UniversityLocalDataSource @Inject constructor(
    private val buildingDAO: BuildingDAO,
    private val roomDAO: RoomDAO,
    private val facultyDAO: FacultyDAO,
    private val groupDAO: GroupDAO,
    private val studentDAO: StudentDAO,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getBuildingEntities(): List<BuildingEntity> =
        withContext(context = coroutineDispatcher) { buildingDAO.getEntities() }

    suspend fun getRoomEntities(buildingID: Long): List<RoomEntity> =
        withContext(context = coroutineDispatcher) { roomDAO.getBuildingRooms(buildingID) }

    suspend fun getFacultyEntities(): List<FacultyEntity> =
        withContext(context = coroutineDispatcher) { facultyDAO.getEntities() }

    suspend fun getGroupEntities(facultyID: Long): List<GroupEntity> =
        withContext(context = coroutineDispatcher) { groupDAO.getFacultyGroups(facultyID) }

    suspend fun getStudentEntities(groupID: Long): List<StudentEntity> =
        withContext(context = coroutineDispatcher) { studentDAO.getStudentsOfGroup(groupID) }

    suspend fun saveBuildingEntities(vararg buildingEntity: BuildingEntity): Unit =
        withContext(context = coroutineDispatcher) { buildingDAO.insertOrUpdate(*buildingEntity) }

    suspend fun saveRoomEntities(vararg roomEntity: RoomEntity): Unit =
        withContext(context = coroutineDispatcher) { roomDAO.insertOrUpdate(*roomEntity) }

    suspend fun saveFacultyEntities(vararg facultyEntity: FacultyEntity): Unit =
        withContext(context = coroutineDispatcher) { facultyDAO.insertOrUpdate(*facultyEntity) }

    suspend fun saveGroupEntities(vararg groupEntity: GroupEntity): Unit =
        withContext(context = coroutineDispatcher) { groupDAO.insertOrUpdate(*groupEntity) }

    suspend fun saveStudentEntities(vararg studentEntity: StudentEntity): Unit =
        withContext(context = coroutineDispatcher) { studentDAO.insertOrUpdate(*studentEntity) }
}
