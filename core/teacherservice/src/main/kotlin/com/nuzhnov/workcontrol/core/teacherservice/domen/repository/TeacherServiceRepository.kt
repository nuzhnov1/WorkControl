package com.nuzhnov.workcontrol.core.teacherservice.domen.repository

import com.nuzhnov.workcontrol.core.teacherservice.domen.model.TeacherServiceState
import kotlinx.coroutines.flow.StateFlow

internal interface TeacherServiceRepository {
    val serviceState: StateFlow<TeacherServiceState>
    val serviceName: StateFlow<String?>


    fun updateServiceState(state: TeacherServiceState)

    fun updateLessonID(id: Long?)

    fun updateServiceName(name: String?)

    suspend fun startControl()
}
