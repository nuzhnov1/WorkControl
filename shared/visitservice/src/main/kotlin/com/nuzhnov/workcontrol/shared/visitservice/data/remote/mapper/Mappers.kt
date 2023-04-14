package com.nuzhnov.workcontrol.shared.visitservice.data.remote.mapper

import com.nuzhnov.workcontrol.shared.visitservice.data.remote.util.VisitorNetworkModel
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.Visitor
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceError
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceError
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServerError
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServerState
import com.nuzhnov.workcontrol.core.visitcontrol.visitor.VisitorError
import com.nuzhnov.workcontrol.core.visitcontrol.visitor.VisitorState


internal fun Visitor.toNetworkModel() = VisitorNetworkModel(
    visitorID = id,
    isActive = isActive,
    lastVisitTime = lastVisitTime,
    totalVisitDuration = totalVisitDuration
)

internal fun VisitorNetworkModel.toModel() = Visitor(
    id = visitorID,
    isActive = isActive,
    lastVisitTime = lastVisitTime,
    totalVisitDuration = totalVisitDuration
)

internal fun Iterable<Visitor>.toNetworkModelSet() = map(Visitor::toNetworkModel).toSet()

internal fun Iterable<VisitorNetworkModel>.toModelSet() = map(VisitorNetworkModel::toModel).toSet()

internal fun ControlServerState.toControlServiceState() = when (this) {
    is ControlServerState.NotRunningYet -> null

    is ControlServerState.Running -> ControlServiceState.Running(address, port)

    is ControlServerState.StoppedAcceptConnections ->
        ControlServiceState.StoppedAcceptConnections(error = error.toVisitorServiceError())

    is ControlServerState.Stopped -> ControlServiceState.Stopped

    is ControlServerState.StoppedByError ->
        ControlServiceState.StoppedByError(error = error.toVisitorServiceError())
}

internal fun VisitorState.toVisitorServiceState() = when (this) {
    is VisitorState.NotRunningYet -> null
    is VisitorState.Connecting -> VisitorServiceState.Connecting
    is VisitorState.Running -> VisitorServiceState.Running
    is VisitorState.Stopped -> VisitorServiceState.Stopped

    is VisitorState.StoppedByError ->
        VisitorServiceState.StoppedByError(error = error.toVisitorServiceError())
}

internal fun ControlServerError.toVisitorServiceError() = when (this) {
    ControlServerError.INIT_ERROR -> ControlServiceError.INIT_ERROR

    ControlServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED ->
        ControlServiceError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED

    ControlServerError.IO_ERROR -> ControlServiceError.IO_ERROR
    ControlServerError.SECURITY_ERROR -> ControlServiceError.SECURITY_ERROR
    ControlServerError.UNKNOWN_ERROR -> ControlServiceError.UNKNOWN_ERROR
}

internal fun VisitorError.toVisitorServiceError() = when (this) {
    VisitorError.CONNECTION_FAILED -> VisitorServiceError.CONNECTION_FAILED_ERROR
    VisitorError.BREAK_CONNECTION -> VisitorServiceError.BREAK_CONNECTION_ERROR
    VisitorError.BAD_CONNECTION -> VisitorServiceError.BAD_CONNECTION_ERROR
    VisitorError.IO_ERROR -> VisitorServiceError.IO_ERROR_ERROR
    VisitorError.SECURITY_ERROR -> VisitorServiceError.SECURITY_ERROR
    VisitorError.UNKNOWN_ERROR -> VisitorServiceError.UNKNOWN_ERROR
}
