package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.database.dao.*
import com.nuzhnov.workcontrol.core.database.entity.*
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class UniversityLocalDataSource @Inject constructor(
    private val buildingDAO: BuildingDAO,
    private val roomDAO: RoomDAO,
    private val facultyDAO: FacultyDAO,
    private val groupDAO: GroupDAO,
    private val studentDAO: StudentDAO,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    fun getBuildingEntitiesFlow(): Flow<List<BuildingEntity>> = buildingDAO
        .getEntitiesFlow()
        .flowOn(context = coroutineDispatcher)

    fun getRoomEntitiesFlow(buildingID: Long): Flow<List<RoomEntity>> = roomDAO
        .getEntitiesFlow(buildingID)
        .flowOn(context = coroutineDispatcher)

    fun getFacultyEntitiesFlow(): Flow<List<FacultyEntity>> = facultyDAO
        .getEntitiesFlow()
        .flowOn(context = coroutineDispatcher)

    fun getGroupEntitiesFlow(facultyID: Long): Flow<List<GroupEntity>> = groupDAO
        .getEntitiesFlow(facultyID)
        .flowOn(context = coroutineDispatcher)

    fun getStudentEntitiesFlow(groupID: Long): Flow<List<StudentEntity>> = studentDAO
        .getEntitiesFlow(groupID)
        .flowOn(context = coroutineDispatcher)

    suspend fun saveBuildingEntities(vararg buildingEntity: BuildingEntity): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            buildingDAO.insertOrUpdate(*buildingEntity)
        }

    suspend fun saveRoomEntities(vararg roomEntity: RoomEntity): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            roomDAO.insertOrUpdate(*roomEntity)
        }

    suspend fun saveFacultyEntities(vararg facultyEntity: FacultyEntity): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            facultyDAO.insertOrUpdate(*facultyEntity)
        }

    suspend fun saveGroupEntities(vararg groupEntity: GroupEntity): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            groupDAO.insertOrUpdate(*groupEntity)
        }

    suspend fun saveStudentEntities(vararg studentEntity: StudentEntity): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            studentDAO.insertOrUpdate(*studentEntity)
        }
}
