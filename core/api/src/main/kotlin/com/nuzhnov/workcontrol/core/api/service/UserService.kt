package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/user/student")
    suspend fun getStudent(@Query("id") id: Long): StudentModelDTO

    @GET("/user/teacher")
    suspend fun getTeacher(@Query("id") id: Long): TeacherModelDTO
}
