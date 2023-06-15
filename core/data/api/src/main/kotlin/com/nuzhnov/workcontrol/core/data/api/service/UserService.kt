package com.nuzhnov.workcontrol.core.data.api.service

import com.nuzhnov.workcontrol.core.data.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.data.api.annotation.PermittedTo
import com.nuzhnov.workcontrol.core.models.Role
import retrofit2.http.GET

interface UserService {
    // Примечание: информация извлекается о текущем авторизованном студенте по токену
    @[GET("/user/student") PermittedTo(Role.STUDENT)]
    suspend fun getStudent(): StudentModelDTO

    // Примечание: информация извлекается о текущем авторизованном преподавателе по токену
    @[GET("/user/teacher") PermittedTo(Role.TEACHER)]
    suspend fun getTeacher(): TeacherModelDTO
}
