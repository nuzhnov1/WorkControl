package com.nuzhnov.workcontrol.core.controlservice

import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientError
import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientState
import com.nuzhnov.workcontrol.core.controlservice.model.Client
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerError
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerState
import java.time.LocalTime


internal fun log(message: String) {
    val now = LocalTime.now()
    println("$now:$message")
}

internal fun Client.toLog() =
    "Client#$id: is active - ${isActive.toLog()}; " +
    "last visit - $lastVisit; " +
    "total visit duration - $totalVisitDuration;"

internal fun ControlServerState.toLog() = when (this) {
    is ControlServerState.NotRunning -> "not running"

    is ControlServerState.Running -> "running on address '${address}' and port '${port}'"

    is ControlServerState.StoppedAcceptConnections ->
        "stopped accepting new connections for the reason: ${error.toLog()}; " +
        "Detailed message: ${cause.toLog()}"

    is ControlServerState.Stopped ->
        "stopped for the reason: ${error.toLog()}; Detailed message: ${cause.toLog()}"
}

internal fun ControlClientState.toLog() = when (this) {
    is ControlClientState.NotRunning -> "not running"

    is ControlClientState.Connecting ->
        "connecting to the server with address '$serverAddress' and port '$serverPort'"

    is ControlClientState.Running ->
        "has successfully connected to the server with address " +
        "'$serverAddress' and port '$serverPort'"

    is ControlClientState.Stopped ->
        "connection to the server with address '$serverAddress' and port '$serverPort' " +
        "has been stopped for the reason: ${error.toLog()}; Detailed message: ${cause.toLog()}"
}

internal fun ControlServerError.toLog() = when (this) {
    ControlServerError.INIT_ERROR -> "initialization failed"
    ControlServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED -> "failed to accept new connections"
    ControlServerError.IO_ERROR -> "an I/O error has occurred"
    ControlServerError.SECURITY_ERROR -> "permission denied to accept a new connection"
    ControlServerError.UNKNOWN_ERROR -> "an unknown error has occurred"
}

internal fun ControlClientError.toLog() =
    when (this) {
        ControlClientError.CONNECTION_FAILED -> "failed to connect to the server"
        ControlClientError.BREAK_CONNECTION -> "the connection to the server has been broken"
        ControlClientError.BAD_CONNECTION -> "bad connection to the server"
        ControlClientError.IO_ERROR -> "an I/O error has occurred when connecting to the server"
        ControlClientError.SECURITY_ERROR -> "permission denied to connect to the server"
        ControlClientError.UNKNOWN_ERROR ->
            "an unknown error has occurred when connecting to the server"
    }

internal fun Throwable.toLog() = when (localizedMessage) {
    null -> "<empty>"
    else -> localizedMessage
}

internal fun Boolean.toLog() = when (this) {
    true -> "yes"
    false -> "no"
}
