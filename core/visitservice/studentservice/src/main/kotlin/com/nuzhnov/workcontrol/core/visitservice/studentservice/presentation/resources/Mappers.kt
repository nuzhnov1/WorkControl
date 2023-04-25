package com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.resources

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceError
import com.nuzhnov.workcontrol.core.visitservice.studentservice.R
import android.content.Context

internal fun StudentServiceState.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            is StudentServiceState.NotInitialized -> getString(
                R.string.student_service_not_initialized_status
            )

            is StudentServiceState.InitFailed -> getString(
                R.string.student_service_init_failed_status
            )

            is StudentServiceState.ReadyToRun -> getString(
                R.string.student_service_ready_to_run_status
            )

            is StudentServiceState.Discovering -> getString(
                R.string.student_service_discovering_status
            )

            is StudentServiceState.Resolving -> getString(
                R.string.student_service_resolving_service_status,
                serviceName
            )

            is StudentServiceState.Connecting -> getString(
                R.string.student_service_connecting_status
            )

            is StudentServiceState.Running -> getString(
                R.string.student_service_running_status
            )

            is StudentServiceState.Stopped -> getString(
                R.string.student_service_stopped_status
            )

            is StudentServiceState.StoppedByError -> getString(
                R.string.student_service_stopped_by_error_status,
                error.toResourceString(context = this)
            )
        }
    }

internal fun StudentServiceError.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            StudentServiceError.DISCOVER_SERVICES_FAILED -> getString(
                R.string.student_service_discovering_services_failed_error
            )

            StudentServiceError.RESOLVE_SERVICE_FAILED -> getString(
                R.string.student_service_resolving_service_failed_error
            )

            StudentServiceError.CONNECTION_FAILED_ERROR -> getString(
                R.string.student_service_connection_failed_error
            )

            StudentServiceError.BREAK_CONNECTION_ERROR -> getString(
                R.string.student_service_break_connection_error
            )

            StudentServiceError.BAD_CONNECTION_ERROR -> getString(
                R.string.student_service_bad_connection_error
            )

            StudentServiceError.DISCONNECTION_ERROR -> getString(
                R.string.student_service_disconnection_error
            )

            StudentServiceError.IO_ERROR -> getString(
                R.string.student_service_IO_error
            )

            StudentServiceError.SECURITY_ERROR -> getString(
                R.string.student_service_security_error
            )

            StudentServiceError.UNKNOWN_ERROR -> getString(
                R.string.student_service_unknown_error
            )
        }
    }
