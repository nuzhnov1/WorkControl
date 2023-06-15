package com.nuzhnov.workcontrol.core.university.data.datasource

import com.nuzhnov.workcontrol.core.data.database.dao.*
import com.nuzhnov.workcontrol.core.data.database.entity.*
import javax.inject.Inject

internal class UniversityLocalDataSource @Inject constructor(
    private val buildingDAO: BuildingDAO,
    private val roomDAO: RoomDAO,
    private val departmentDAO: DepartmentDAO,
    private val groupDAO: GroupDAO,
    private val studentDAO: StudentDAO
) {

    fun getBuildingEntitiesFlow() = buildingDAO.getEntitiesFlow()

    fun getRoomsFlow(buildingID: Long) = roomDAO.getEntitiesFlow(buildingID)

    fun getDepartmentEntitiesFlow() = departmentDAO.getEntitiesFlow()

    fun getGroupEntitiesFlow(departmentID: Long) = groupDAO.getEntitiesFlow(departmentID)

    fun getStudentEntitiesFlow(groupID: Long) = studentDAO.getEntitiesFlow(groupID)

    suspend fun saveBuildingEntities(vararg buildingEntity: BuildingEntity) =
        buildingDAO.insertOrUpdate(*buildingEntity)

    suspend fun saveRoomEntities(vararg roomEntity: RoomEntity) =
        roomDAO.insertOrUpdate(*roomEntity)

    suspend fun saveDepartmentEntities(vararg departmentEntity: DepartmentEntity) =
        departmentDAO.insertOrUpdate(*departmentEntity)

    suspend fun saveGroupEntities(vararg groupEntity: GroupEntity) =
        groupDAO.insertOrUpdate(*groupEntity)

    suspend fun saveStudentEntities(vararg studentEntity: StudentEntity) =
        studentDAO.insertOrUpdate(*studentEntity)
}
