package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.session.data.mapper.toUserData
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.data.api.service.UserService
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.model.Role
import javax.inject.Inject

internal class UserRemoteDataSource @Inject constructor(private val userService: UserService) {

    suspend fun getUserData(role: Role): Response<UserData> = safeApiCall {
        when (role) {
            Role.TEACHER -> userService.getTeacher().toUserData()
            Role.STUDENT -> userService.getStudent().toUserData()
        }
    }
}
