package com.nuzhnov.workcontrol.core.session.data.datasource

import com.nuzhnov.workcontrol.core.session.data.mapper.toUserData
import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.api.service.UserService
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class UserRemoteDataSource @Inject constructor(
    private val userService: UserService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getUserData(role: Role): Response<UserData> =
        safeApiCall(context = coroutineDispatcher) {
            when (role) {
                Role.TEACHER -> userService.getTeacher().toUserData()
                Role.STUDENT -> userService.getStudent().toUserData()
            }
        }
}
