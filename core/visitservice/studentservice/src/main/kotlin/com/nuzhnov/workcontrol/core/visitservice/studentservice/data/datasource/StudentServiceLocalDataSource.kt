package com.nuzhnov.workcontrol.core.visitservice.studentservice.data.datasource

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class StudentServiceLocalDataSource @Inject constructor() {
    private val _serviceState = MutableStateFlow<StudentServiceState>(value = NotInitialized)
    val serviceState = _serviceState.asStateFlow()

    private val _studentID = MutableStateFlow<Long?>(value = null)
    val studentID = _studentID.asStateFlow()


    fun updateServiceState(state: StudentServiceState) {
        val currentState = _serviceState.value

        val currentStateIsStopped = currentState is Stopped || currentState is StoppedByError
        val newStateIsStopped = state is Stopped || state is StoppedByError

        if (currentStateIsStopped && newStateIsStopped) {
            return
        } else {
            _serviceState.value = state
        }
    }

    fun updateStudentID(id: Long?) {
        _studentID.value = id
    }
}
