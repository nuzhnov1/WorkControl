package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.api.annotation.PermittedTo
import com.nuzhnov.workcontrol.core.model.Role
import retrofit2.http.*

interface UniversityService {
    @[GET("/buildings") PermittedTo(Role.TEACHER)]
    suspend fun getBuildings(): List<BuildingDTO>

    @[GET("/rooms") PermittedTo(Role.TEACHER)]
    suspend fun getBuildingRooms(@Query("building_id") buildingID: Long): List<RoomDTO>

    @[GET("/faculties") PermittedTo(Role.TEACHER)]
    suspend fun getFaculties(): List<FacultyDTO>

    @[GET("/groups") PermittedTo(Role.TEACHER)]
    suspend fun getFacultyGroups(@Query("faculty_id") facultyID: Long): List<GroupDTO>

    @[GET("/students") PermittedTo(Role.TEACHER)]
    suspend fun getStudentsOfGroup(@Query("group_id") groupID: Long): List<StudentDTO>

    @[POST("/students?list") PermittedTo(Role.TEACHER)]
    suspend fun getStudentsOfGroups(@Body groupIDList: List<Long>): Map<Long, StudentDTO>
}
