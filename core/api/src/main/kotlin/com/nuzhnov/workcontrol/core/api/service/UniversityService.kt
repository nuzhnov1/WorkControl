package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.university.*
import retrofit2.http.*

interface UniversityService {
    @GET("/buildings")
    suspend fun getBuildings(): List<BuildingDTO>

    @GET("/rooms")
    suspend fun getBuildingRooms(@Query("building_id") buildingID: Long): List<RoomDTO>

    @GET("/faculties")
    suspend fun getFaculties(): List<FacultyDTO>

    @GET("/groups")
    suspend fun getFacultyGroups(@Query("faculty_id") facultyID: Long): List<GroupDTO>

    @GET("/students")
    suspend fun getStudentsOfGroup(@Query("group_id") groupID: Long): List<StudentDTO>

    @POST("/students?list")
    suspend fun getStudentsOfGroups(@Body groupIDList: List<Long>): Map<Long, StudentDTO>
}
