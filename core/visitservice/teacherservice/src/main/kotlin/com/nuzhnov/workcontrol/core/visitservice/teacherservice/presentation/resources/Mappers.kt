package com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.resources

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceError
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.R
import android.content.Context


internal fun TeacherServiceState.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            is TeacherServiceState.NotInitialized -> getString(
                R.string.teacher_service_not_initialized_status
            )

            is TeacherServiceState.InitFailed -> getString(
                R.string.teacher_service_init_failed_status
            )

            is TeacherServiceState.ReadyToRun -> getString(
                R.string.teacher_service_ready_to_run_status
            )

            is TeacherServiceState.Running -> getString(
                R.string.teacher_service_running_status
            )

            is TeacherServiceState.StoppedAcceptConnections -> getString(
                R.string.teacher_service_stopped_by_error_status
            )

            is TeacherServiceState.Stopped -> getString(
                R.string.teacher_service_stopped_status
            )

            is TeacherServiceState.StoppedByError -> getString(
                R.string.teacher_service_stopped_by_error_status,
                error.toResourceString(context = this)
            )
        }
    }

internal fun TeacherServiceError.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            TeacherServiceError.REGISTER_SERVICE_FAILED -> getString(
                R.string.teacher_service_register_failed_error
            )

            TeacherServiceError.INIT_ERROR -> getString(
                R.string.teacher_service_init_failed_error
            )

            TeacherServiceError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED -> getString(
                R.string.teacher_service_max_accept_connections_attempts_reached_error
            )

            TeacherServiceError.IO_ERROR -> getString(
                R.string.teacher_service_IO_error
            )

            TeacherServiceError.SECURITY_ERROR -> getString(
                R.string.teacher_service_security_error
            )

            TeacherServiceError.UNKNOWN_ERROR -> getString(
                R.string.teacher_service_unknown_error
            )
        }
    }
