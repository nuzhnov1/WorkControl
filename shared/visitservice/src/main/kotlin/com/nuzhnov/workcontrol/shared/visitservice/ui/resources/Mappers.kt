package com.nuzhnov.workcontrol.shared.visitservice.ui.resources

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceError
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceError
import com.nuzhnov.workcontrol.shared.visitservice.R
import android.content.Context


internal fun ControlServiceState.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            is ControlServiceState.NotInitialized -> getString(
                R.string.control_service_not_initialized_status
            )

            is ControlServiceState.InitFailed -> getString(
                R.string.control_service_init_failed_status
            )

            is ControlServiceState.ReadyToRun -> getString(
                R.string.control_service_ready_to_run_status
            )

            is ControlServiceState.Running -> getString(
                R.string.control_service_running_status
            )

            is ControlServiceState.StoppedAcceptConnections -> getString(
                R.string.control_service_stopped_by_error_status
            )

            is ControlServiceState.Stopped -> getString(
                R.string.control_service_stopped_status
            )

            is ControlServiceState.StoppedByError -> getString(
                R.string.control_service_stopped_by_error_status,
                error.toResourceString(context = this)
            )
        }
    }

internal fun VisitorServiceState.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            is VisitorServiceState.NotInitialized -> getString(
                R.string.visitor_service_not_initialized_status
            )

            is VisitorServiceState.InitFailed -> getString(
                R.string.visitor_service_init_failed_status
            )

            is VisitorServiceState.ReadyToRun -> getString(
                R.string.visitor_service_ready_to_run_status
            )

            is VisitorServiceState.Discovering -> getString(
                R.string.visitor_service_discovering_status
            )

            is VisitorServiceState.Resolving -> getString(
                R.string.visitor_service_resolving_service_status,
                serviceName
            )

            is VisitorServiceState.Connecting -> getString(
                R.string.visitor_service_connecting_status
            )

            is VisitorServiceState.Running -> getString(
                R.string.visitor_service_running_status
            )

            is VisitorServiceState.Stopped -> getString(
                R.string.visitor_service_stopped_status
            )

            is VisitorServiceState.StoppedByError -> getString(
                R.string.visitor_service_stopped_by_error_status,
                error.toResourceString(context = this)
            )
        }
    }

internal fun ControlServiceError.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            ControlServiceError.REGISTER_SERVICE_FAILED -> getString(
                R.string.control_service_register_failed_error
            )

            ControlServiceError.INIT_ERROR -> getString(
                R.string.control_service_init_failed_error
            )

            ControlServiceError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED -> getString(
                R.string.control_service_max_accept_connections_attempts_reached_error
            )

            ControlServiceError.IO_ERROR -> getString(
                R.string.control_service_IO_error
            )

            ControlServiceError.SECURITY_ERROR -> getString(
                R.string.control_service_security_error
            )

            ControlServiceError.UNKNOWN_ERROR -> getString(
                R.string.control_service_unknown_error
            )
        }
    }

internal fun VisitorServiceError.toResourceString(context: Context): String = context
    .applicationContext
    .run {
        when (this@toResourceString) {
            VisitorServiceError.DISCOVER_SERVICES_FAILED -> getString(
                R.string.visitor_service_discovering_services_failed_error
            )

            VisitorServiceError.RESOLVE_SERVICE_FAILED -> getString(
                R.string.visitor_service_resolving_service_failed_error
            )

            VisitorServiceError.CONNECTION_FAILED_ERROR -> getString(
                R.string.visitor_service_connection_failed_error
            )

            VisitorServiceError.BREAK_CONNECTION_ERROR -> getString(
                R.string.visitor_service_break_connection_error
            )

            VisitorServiceError.BAD_CONNECTION_ERROR -> getString(
                R.string.visitor_service_bad_connection_error
            )

            VisitorServiceError.IO_ERROR -> getString(
                R.string.visitor_service_IO_error
            )

            VisitorServiceError.SECURITY_ERROR -> getString(
                R.string.visitor_service_security_error
            )

            VisitorServiceError.UNKNOWN_ERROR -> getString(
                R.string.visitor_service_unknown_error
            )
        }
    }
