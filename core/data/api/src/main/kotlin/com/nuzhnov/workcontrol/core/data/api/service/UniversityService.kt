package com.nuzhnov.workcontrol.core.data.api.service

import com.nuzhnov.workcontrol.core.data.api.dto.university.*
import com.nuzhnov.workcontrol.core.data.api.annotation.PermittedTo
import com.nuzhnov.workcontrol.core.models.Role
import retrofit2.http.*

interface UniversityService {
    @[GET("/buildings") PermittedTo(Role.TEACHER)]
    suspend fun getBuildings(): List<BuildingDTO>

    @[GET("/rooms") PermittedTo(Role.TEACHER)]
    suspend fun getBuildingRooms(@Query("building_id") buildingID: Long): List<RoomDTO>

    @[GET("/departments") PermittedTo(Role.TEACHER)]
    suspend fun getDepartments(): List<DepartmentDTO>

    @[GET("/groups") PermittedTo(Role.TEACHER)]
    suspend fun getDepartmentGroups(@Query("department_id") departmentID: Long): List<GroupDTO>

    @[GET("/students") PermittedTo(Role.TEACHER)]
    suspend fun getStudentsOfGroup(@Query("group_id") groupID: Long): List<StudentDTO>

    @[POST("/students?list") PermittedTo(Role.TEACHER)]
    suspend fun getStudentsOfGroups(@Body groupIDList: List<Long>): Map<Long, StudentDTO>
}
