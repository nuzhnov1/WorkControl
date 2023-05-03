package com.nuzhnov.workcontrol.core.visitservice.studentservice.data.mapper

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.DiscoveredService
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceError
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorState
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorError
import android.net.nsd.NsdServiceInfo


internal fun Map<String, NsdServiceInfo>.toDiscoveredServicesSet(): Set<DiscoveredService> =
    values.map { nsdServiceInfo -> nsdServiceInfo.toDiscoveredService() }.toSet()

internal fun NsdServiceInfo.toDiscoveredService(): DiscoveredService = DiscoveredService(
    name = serviceName,
    host = host,
    port = port
)

internal fun VisitorState.toServiceState(): StudentServiceState? = when (this) {
    is VisitorState.NotRunningYet   -> null
    is VisitorState.Connecting      -> StudentServiceState.Connecting
    is VisitorState.Running         -> StudentServiceState.Running
    is VisitorState.Stopped         -> StudentServiceState.Stopped

    is VisitorState.StoppedByError  ->
        StudentServiceState.StoppedByError(error = error.toVisitorServiceError())
}

internal fun VisitorError.toVisitorServiceError(): StudentServiceError = when (this) {
    VisitorError.CONNECTION_FAILED  -> StudentServiceError.CONNECTION_FAILED_ERROR
    VisitorError.BREAK_CONNECTION   -> StudentServiceError.BREAK_CONNECTION_ERROR
    VisitorError.BAD_CONNECTION     -> StudentServiceError.BAD_CONNECTION_ERROR
    VisitorError.DISCONNECTED       -> StudentServiceError.DISCONNECTION_ERROR
    VisitorError.IO_ERROR           -> StudentServiceError.IO_ERROR
    VisitorError.SECURITY_ERROR     -> StudentServiceError.SECURITY_ERROR
    VisitorError.UNKNOWN_ERROR      -> StudentServiceError.UNKNOWN_ERROR
}
