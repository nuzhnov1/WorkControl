package com.nuzhnov.workcontrol.core.visitcontrol

import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientError
import com.nuzhnov.workcontrol.core.visitcontrol.client.ClientState
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorDebug
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerError
import com.nuzhnov.workcontrol.core.visitcontrol.server.ServerState
import org.joda.time.LocalTime


internal fun log(message: String) {
    val now = LocalTime.now()
    println("$now:$message")
}

internal fun VisitorDebug.toLog() =
    "Visitor#$id: is active - ${isActive.toLog()}; " +
    "last visit - $lastVisit; " +
    "total visit duration - $totalVisitDuration;"

internal fun ServerState.toLog() = when (this) {
    is ServerState.NotRunning -> "not running"

    is ServerState.Running -> "running on address '${address}' and port '${port}'"

    is ServerState.StoppedAcceptConnections ->
        "stopped accepting new connections for the reason: ${error.toLog()}; " +
        "Detailed message: ${cause.toLog()}"

    is ServerState.Stopped -> "stopped"

    is ServerState.StoppedByError ->
        "stopped by the error: ${error.toLog()}; Detailed message: ${cause.toLog()}"
}

internal fun ClientState.toLog() = when (this) {
    is ClientState.NotRunning -> "not running"

    is ClientState.Connecting ->
        "connecting to the server with address '$serverAddress' and port '$serverPort'"

    is ClientState.Running ->
        "has successfully connected to the server with address " +
        "'$serverAddress' and port '$serverPort'"

    is ClientState.Stopped ->
        "connection to the server with address '$serverAddress' and port '$serverPort' " +
        "has been stopped"

    is ClientState.StoppedByError ->
        "connection to the server with address '$serverAddress' and port '$serverPort' " +
        "has been stopped by the error: ${error.toLog()}; Detailed message: ${cause.toLog()}"
}

internal fun ServerError.toLog() = when (this) {
    ServerError.INIT_ERROR -> "initialization failed"
    ServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED -> "failed to accept new connections"
    ServerError.IO_ERROR -> "an I/O error has occurred"
    ServerError.SECURITY_ERROR -> "permission denied to accept a new connection"
    ServerError.UNKNOWN_ERROR -> "an unknown error has occurred"
}

internal fun ClientError.toLog() =
    when (this) {
        ClientError.CONNECTION_FAILED -> "failed to connect to the server"
        ClientError.BREAK_CONNECTION -> "the connection to the server has been broken"
        ClientError.BAD_CONNECTION -> "bad connection to the server"
        ClientError.IO_ERROR -> "an I/O error has occurred when connecting to the server"
        ClientError.SECURITY_ERROR -> "permission denied to connect to the server"
        ClientError.UNKNOWN_ERROR ->
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
