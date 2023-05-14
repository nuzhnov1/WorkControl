package com.nuzhnov.workcontrol.core.data.api.service

import com.nuzhnov.workcontrol.core.data.api.dto.statistics.StatisticsDTO
import com.nuzhnov.workcontrol.core.data.api.annotation.PermittedTo
import com.nuzhnov.workcontrol.core.model.Role
import retrofit2.http.GET
import retrofit2.http.Query

interface StatisticsService {
    @[GET("/stat?all") PermittedTo(Role.TEACHER)]
    suspend fun getUniversityStatistics(): StatisticsDTO

    @[GET("/stat") PermittedTo(Role.TEACHER)]
    suspend fun getFacultyStatistics(@Query("faculty_id") facultyID: Long): StatisticsDTO

    @[GET("/stat") PermittedTo(Role.TEACHER)]
    suspend fun getGroupStatistics(@Query("group_id") groupID: Long): StatisticsDTO

    @[GET("/stat") PermittedTo(Role.TEACHER)]
    suspend fun getStudentStatistics(@Query("student_id") studentID: Long): StatisticsDTO

    // Примечание: статистика студента извлекается по его токену
    @[GET("/stat") PermittedTo(Role.STUDENT)]
    suspend fun getStudentStatistics(): StatisticsDTO
}
