package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.datasource

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class TeacherServiceLocalDataSource @Inject constructor() {
    private val _serviceState = MutableStateFlow<TeacherServiceState>(value = NotInitialized)
    val serviceState = _serviceState.asStateFlow()

    private val _lessonID = MutableStateFlow<Long?>(value = null)
    val lessonID = _lessonID.asStateFlow()

    private val _serviceName = MutableStateFlow<String?>(value = null)
    val serviceName = _serviceName.asStateFlow()


    fun updateServiceState(state: TeacherServiceState) {
        val currentState = _serviceState.value

        val currentStateIsStopped = currentState is Stopped || currentState is StoppedByError
        val newStateIsStopped = state is Stopped || state is StoppedByError

        if (currentStateIsStopped && newStateIsStopped) {
            return
        } else {
            _serviceState.value = state
        }
    }

    fun updateLessonID(id: Long?) {
        _lessonID.value = id
    }

    fun updateServiceName(name: String?) {
        _serviceName.value = name
    }
}
