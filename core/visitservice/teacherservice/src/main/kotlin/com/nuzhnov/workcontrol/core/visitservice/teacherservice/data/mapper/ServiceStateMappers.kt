package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.mapper

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceError
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerError


internal fun ControlServerState.toServiceState(): TeacherServiceState? = when (this) {
    is ControlServerState.NotRunningYet -> null

    is ControlServerState.Running -> TeacherServiceState.Running(address, port)

    is ControlServerState.StoppedAcceptConnections ->
        TeacherServiceState.StoppedAcceptConnections(error = error.toServiceError())

    is ControlServerState.Stopped -> TeacherServiceState.Stopped

    is ControlServerState.StoppedByError ->
        TeacherServiceState.StoppedByError(error = error.toServiceError())
}

private fun ControlServerError.toServiceError(): TeacherServiceError = when (this) {
    ControlServerError.INIT_ERROR -> TeacherServiceError.INIT_ERROR

    ControlServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED ->
        TeacherServiceError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED

    ControlServerError.IO_ERROR -> TeacherServiceError.IO_ERROR
    ControlServerError.SECURITY_ERROR -> TeacherServiceError.SECURITY_ERROR
    ControlServerError.UNKNOWN_ERROR -> TeacherServiceError.UNKNOWN_ERROR
}
