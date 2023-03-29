package com.nuzhnov.workcontrol.shared.visitservice.data.mapper

import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientError
import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientState
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerError
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceStopReason
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceStopReason


internal fun ServerState.toVisitControlServiceState() = when (this) {
    is ServerState.NotRunning -> VisitControlServiceState.NotRunning
    is ServerState.Running -> VisitControlServiceState.Running
    is ServerState.StoppedAcceptConnections -> VisitControlServiceState.StoppedAcceptConnections

    is ServerState.Stopped -> VisitControlServiceState.Stopped(
        reason = error.toVisitControlServiceStopReason()
    )
}

internal fun ClientState.toVisitClientServiceState() = when (this) {
    is ClientState.NotRunning -> VisitClientServiceState.NotRunning
    is ClientState.Connecting -> VisitClientServiceState.Connecting
    is ClientState.Running -> VisitClientServiceState.Running

    is ClientState.Stopped -> VisitClientServiceState.Stopped(
        reason = error.toVisitControlServiceStopReason()
    )
}

internal fun ServerError.toVisitControlServiceStopReason() = when (this) {
    ServerError.INIT_ERROR -> VisitControlServiceStopReason.INIT_ERROR

    ServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED ->
        VisitControlServiceStopReason.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED

    ServerError.IO_ERROR -> VisitControlServiceStopReason.IO_ERROR
    ServerError.SECURITY_ERROR -> VisitControlServiceStopReason.SECURITY_ERROR
    ServerError.UNKNOWN_ERROR -> VisitControlServiceStopReason.UNKNOWN_ERROR
}

internal fun ClientError.toVisitControlServiceStopReason() = when (this) {
    ClientError.CONNECTION_FAILED -> VisitClientServiceStopReason.CONNECTION_FAILED_ERROR
    ClientError.BREAK_CONNECTION -> VisitClientServiceStopReason.BREAK_CONNECTION_ERROR
    ClientError.BAD_CONNECTION -> VisitClientServiceStopReason.BAD_CONNECTION_ERROR
    ClientError.IO_ERROR -> VisitClientServiceStopReason.IO_ERROR_ERROR
    ClientError.SECURITY_ERROR -> VisitClientServiceStopReason.SECURITY_ERROR
    ClientError.UNKNOWN_ERROR -> VisitClientServiceStopReason.UNKNOWN_ERROR
}
