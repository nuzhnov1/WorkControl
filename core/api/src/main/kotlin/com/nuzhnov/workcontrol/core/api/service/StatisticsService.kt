package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.statistics.StatisticsDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface StatisticsService {
    @GET("/stat")
    suspend fun getFacultyStatistics(@Query("faculty_id") facultyID: Long): StatisticsDTO

    @GET("/stat")
    suspend fun getGroupStatistics(@Query("group_id") groupID: Long): StatisticsDTO

    @GET("/stat")
    suspend fun getStudentStatistics(@Query("student_id") studentID: Long): StatisticsDTO
}
