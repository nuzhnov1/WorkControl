package com.nuzhnov.workcontrol.common.visitcontrol

import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerError
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitDebug
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorError
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.VisitorState
import com.soywiz.klock.DateTime


internal fun log(message: String) {
    val now = DateTime.nowLocal().local.time
    println("$now: $message")
}

internal fun Collection<VisitDebug>.log() {
    println("Visitors info:")
    println("Visitors count: ${size}.")
    forEach { visit -> println(visit.toLog()) }
}

internal fun VisitDebug.toLog() =
    "Visitor#$visitorID: is active - ${isActive.toLog()}; " +
    "last visit - $lastVisitTime; " +
    "total visit duration - $totalVisitDuration;"

internal fun ControlServerState.toLog() = when (this) {
    is ControlServerState.NotRunningYet -> "not running"

    is ControlServerState.Running -> "running on address '${address}' and port '${port}'"

    is ControlServerState.StoppedAcceptConnections ->
        "stopped accepting new connections for the reason: ${error.toLog()}; " +
        "Detailed message: ${cause.toLog()}"

    is ControlServerState.Stopped -> "stopped"

    is ControlServerState.StoppedByError ->
        "stopped by the error: ${error.toLog()}; Detailed message: ${cause.toLog()}; " +
        "Exception: $cause"
}

internal fun VisitorState.toLog() = when (this) {
    is VisitorState.NotRunningYet -> "not running"

    is VisitorState.Connecting ->
        "connecting to the server with address '$serverAddress' and port '$serverPort'"

    is VisitorState.Running ->
        "has successfully connected to the server with address " +
        "'$serverAddress' and port '$serverPort'"

    is VisitorState.Stopped ->
        "connection to the server with address '$serverAddress' and port '$serverPort' " +
        "has been stopped"

    is VisitorState.StoppedByError ->
        "connection to the server with address '$serverAddress' and port '$serverPort' " +
        "has been stopped by the error: ${error.toLog()}; Detailed message: ${cause.toLog()}; " +
        "Exception: $cause"
}

internal fun ControlServerError.toLog() = when (this) {
    ControlServerError.INIT_ERROR -> "initialization failed"
    ControlServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED -> "failed to accept new connections"
    ControlServerError.IO_ERROR -> "an I/O error has occurred"
    ControlServerError.SECURITY_ERROR -> "permission denied to accept a new connection"
    ControlServerError.UNKNOWN_ERROR -> "an unknown error has occurred"
}

internal fun VisitorError.toLog() =
    when (this) {
        VisitorError.CONNECTION_FAILED -> "failed to connect to the server"
        VisitorError.BREAK_CONNECTION -> "the connection to the server has been broken"
        VisitorError.BAD_CONNECTION -> "bad connection to the server"
        VisitorError.DISCONNECTED -> "the visitor was disconnected"
        VisitorError.IO_ERROR -> "an I/O error has occurred when connecting to the server"
        VisitorError.SECURITY_ERROR -> "permission denied to connect to the server"
        VisitorError.UNKNOWN_ERROR -> "an unknown error has occurred when connecting to the server"
    }

internal fun Throwable.toLog() = when (localizedMessage) {
    null -> "<empty>"
    else -> localizedMessage
}

internal fun Boolean.toLog() = when (this) {
    true -> "yes"
    false -> "no"
}
