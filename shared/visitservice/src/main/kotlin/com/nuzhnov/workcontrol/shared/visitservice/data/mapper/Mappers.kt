package com.nuzhnov.workcontrol.shared.visitservice.data.mapper

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceError
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitClientServiceError
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerError
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerState
import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientError
import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientState


internal fun ServerState.toVisitControlServiceState() = when (this) {
    is ServerState.NotRunning -> VisitControlServiceState.NotRunning

    is ServerState.Running -> VisitControlServiceState.Running(
        serverAddress = address,
        serverPort = port
    )

    is ServerState.StoppedAcceptConnections -> VisitControlServiceState.StoppedAcceptConnections(
        serverAddress = address,
        serverPort = port,
        error = error.toVisitControlServiceError()
    )

    is ServerState.Stopped -> VisitControlServiceState.Stopped(
        serverAddress = address,
        serverPort = port
    )

    is ServerState.StoppedByError -> VisitControlServiceState.StoppedByError(
        error = error.toVisitControlServiceError()
    )
}

internal fun ClientState.toVisitClientServiceState() = when (this) {
    is ClientState.NotRunning -> VisitClientServiceState.NotRunning

    is ClientState.Connecting -> VisitClientServiceState.Connecting(
        serverAddress = serverAddress,
        serverPort = serverPort,
        clientID = visitorID
    )

    is ClientState.Running -> VisitClientServiceState.Running(
        serverAddress = serverAddress,
        serverPort = serverPort,
        clientID = visitorID
    )

    is ClientState.Stopped -> VisitClientServiceState.Stopped(
        serverAddress = serverAddress,
        serverPort = serverPort,
        clientID = visitorID
    )

    is ClientState.StoppedByError -> VisitClientServiceState.StoppedByError(
        error = error.toVisitControlServiceError()
    )
}

internal fun ServerError.toVisitControlServiceError() = when (this) {
    ServerError.INIT_ERROR -> VisitControlServiceError.INIT_ERROR

    ServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED ->
        VisitControlServiceError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED

    ServerError.IO_ERROR -> VisitControlServiceError.IO_ERROR
    ServerError.SECURITY_ERROR -> VisitControlServiceError.SECURITY_ERROR
    ServerError.UNKNOWN_ERROR -> VisitControlServiceError.UNKNOWN_ERROR
}

internal fun ClientError.toVisitControlServiceError() = when (this) {
    ClientError.CONNECTION_FAILED -> VisitClientServiceError.CONNECTION_FAILED_ERROR
    ClientError.BREAK_CONNECTION -> VisitClientServiceError.BREAK_CONNECTION_ERROR
    ClientError.BAD_CONNECTION -> VisitClientServiceError.BAD_CONNECTION_ERROR
    ClientError.IO_ERROR -> VisitClientServiceError.IO_ERROR_ERROR
    ClientError.SECURITY_ERROR -> VisitClientServiceError.SECURITY_ERROR
    ClientError.UNKNOWN_ERROR -> VisitClientServiceError.UNKNOWN_ERROR
}
