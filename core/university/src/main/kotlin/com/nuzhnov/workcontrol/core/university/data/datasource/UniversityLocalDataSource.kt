package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.data.database.dao.*
import com.nuzhnov.workcontrol.core.data.database.entity.*
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UniversityLocalDataSource @Inject constructor(
    private val buildingDAO: BuildingDAO,
    private val roomDAO: RoomDAO,
    private val departmentDAO: DepartmentDAO,
    private val groupDAO: GroupDAO,
    private val studentDAO: StudentDAO
) {

    fun getBuildingEntitiesFlow(): Flow<List<BuildingEntity>> =
        buildingDAO.getEntitiesFlow()

    fun getRoomEntitiesFlow(buildingID: Long): Flow<List<RoomEntity>> =
        roomDAO.getEntitiesFlow(buildingID)

    fun getDepartmentEntitiesFlow(): Flow<List<DepartmentEntity>> =
        departmentDAO.getEntitiesFlow()

    fun getGroupEntitiesFlow(departmentID: Long): Flow<List<GroupEntity>> =
        groupDAO.getEntitiesFlow(departmentID)

    fun getStudentEntitiesFlow(groupID: Long): Flow<List<StudentEntity>> =
        studentDAO.getEntitiesFlow(groupID)

    suspend fun saveBuildingEntities(vararg buildingEntity: BuildingEntity): Result<Unit> =
        safeExecute { buildingDAO.insertOrUpdate(*buildingEntity) }

    suspend fun saveRoomEntities(vararg roomEntity: RoomEntity): Result<Unit> =
        safeExecute { roomDAO.insertOrUpdate(*roomEntity) }

    suspend fun saveDepartmentEntities(vararg departmentEntity: DepartmentEntity): Result<Unit> =
        safeExecute { departmentDAO.insertOrUpdate(*departmentEntity) }

    suspend fun saveGroupEntities(vararg groupEntity: GroupEntity): Result<Unit> =
        safeExecute { groupDAO.insertOrUpdate(*groupEntity) }

    suspend fun saveStudentEntities(vararg studentEntity: StudentEntity): Result<Unit> =
        safeExecute { studentDAO.insertOrUpdate(*studentEntity) }
}
