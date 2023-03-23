package com.nuzhnov.workcontrol.core.controlservice

import com.nuzhnov.workcontrol.core.controlservice.model.Client
import com.nuzhnov.workcontrol.core.controlservice.mapper.toClient
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerError
import com.nuzhnov.workcontrol.core.controlservice.server.ControlServerState
import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientError
import com.nuzhnov.workcontrol.core.controlservice.client.ControlClientState
import kotlin.random.Random
import kotlin.random.nextLong
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import java.net.InetAddress
import java.util.*


const val RANDOM_SEED = 0
const val DELAY_AFTER_FINISH_MS = 1_000L
const val SERVER_WORK_TIME_MS = 10_000L
const val CLIENT_MIN_ID = 0L
const val CLIENT_MAX_ID = 1000L
const val CLIENT_MIN_WORK_TIME_MS = 2_000L
const val CLIENT_MAX_WORK_TIME_MS = 5_000L
const val CLIENT_MIN_APPEARANCE_DELAY_TIME_MS = 0L
const val CLIENT_MAX_APPEARANCE_DELAY_TIME_MS = 1_000L


private val random = Random(RANDOM_SEED)

private val Client.isNotActive get() = !isActive


fun main() {
    val controlServer = ControlServiceApi.getDefaultControlServer()

    repeat(1) {
        controlServer.testCase(clientsCount = 5)
        println()
    }
}

private fun ControlServiceApi.testCase(clientsCount: Int) {
    println("Start testing...")
    println("-".repeat(n = 80))

    runBlocking(Dispatchers.IO) {
        val serverAddress = InetAddress.getLocalHost()
        val serverPort = 48791

        launch { startServerTask(address = serverAddress, port = serverPort) }
        launch { startClients(count = clientsCount, serverAddress, serverPort) }
    }

    println("End testing.")
    println("Clients info:")
    logClientsInfo()
    println("-".repeat( n = 80))
}

private suspend fun ControlServiceApi.startServerTask(
    address: InetAddress,
    port: Int
) = coroutineScope {
    launch {
        var isCreated = false

        serverState.collect { state ->
            if (!isCreated) {
                println("Control Server: created.")
                isCreated = true
            }

            println("Control Server: ${state.toLog()}.")
        }
    }

    launch {
        var currentClientSet: SortedSet<Client> = sortedSetOf()

        clients
            .map { modelsSet -> modelsSet.map { model -> model.toClient() }.toSortedSet() }
            .collect { clientSet ->
                onUpdateClients(currentClientSet, clientSet)
                currentClientSet = clientSet
            }
    }

    runCatching { withTimeout(SERVER_WORK_TIME_MS) { startServer(address, port) } }

    delay(DELAY_AFTER_FINISH_MS)
    cancel()
}

private suspend fun startClients(
    count: Int,
    serverAddress: InetAddress,
    serverPort: Int
) = coroutineScope {
    repeat(count) {
        val controlClient = ControlServiceApi.getDefaultControlServer()
        val clientID = random.nextLong(range = CLIENT_MIN_ID..CLIENT_MAX_ID)

        val clientWorkTimeMillis = random.nextLong(
            range = CLIENT_MIN_WORK_TIME_MS..CLIENT_MAX_WORK_TIME_MS
        )

        val clientAppearanceDelayTimeMillis = random.nextLong(
            range = CLIENT_MIN_APPEARANCE_DELAY_TIME_MS..CLIENT_MAX_APPEARANCE_DELAY_TIME_MS
        )


        delay(timeMillis = clientAppearanceDelayTimeMillis)

        launch {
            launch {
                var isCreated = false

                controlClient.clientState.collect { state ->
                    if (!isCreated) {
                        println("Client#$clientID: created.")
                        isCreated = true
                    }

                    println("Client#$clientID: ${state.toLog()}.")
                }
            }

            runCatching {
                withTimeout(timeMillis = clientWorkTimeMillis) {
                    controlClient.startClient(serverAddress, serverPort, clientID)
                }
            }

            delay(DELAY_AFTER_FINISH_MS)
            cancel()
        }
    }
}

private fun ControlServiceApi.logClientsInfo() = clients.value
    .map { model -> model.toClient() }
    .forEach { client -> println(client.toLog()) }

private fun onUpdateClients(oldClientSet: SortedSet<Client>, newClientSet: SortedSet<Client>) {
    infix fun Boolean.but(other: Boolean) = this && other

    val newClients = newClientSet - oldClientSet

    newClients.forEach { client -> println("Control Server: new client with id = ${client.id}!") }
    oldClientSet.zip(newClientSet).forEach { (oldInfo, newInfo) ->
        if (oldInfo.isActive but newInfo.isNotActive) {
            println("Control Server: the client with id = ${newInfo.id} is not active now.")
        } else if (oldInfo.isNotActive but newInfo.isActive) {
            println("Control Server: the client with id = ${newInfo.id} is active now.")
        }
    }
}

private fun Client.toLog() =
    "Client#$id: is active - ${isActive.toLog()}; " +
    "total visit duration - $totalVisitDuration"

private fun ControlServerState.toLog() = when (this) {
    is ControlServerState.NotRunning -> "not running"

    is ControlServerState.Running -> "running on address '${address}' and port '${port}'"

    is ControlServerState.StoppedAcceptConnections ->
        "stopped accepting new connections for the reason: ${error.toLog()}; " +
        "Detailed message: ${cause.toLog()}"

    is ControlServerState.Stopped ->
        "stopped for the reason: ${error.toLog()}; Detailed message: ${cause.toLog()}"
}

private fun ControlClientState.toLog() = when (this) {
    is ControlClientState.NotRunning -> "not running"

    is ControlClientState.Connecting ->
        "connecting to the server with address '$serverAddress' and port '$serverPort'"

    is ControlClientState.Running ->
        "has successfully connected to the server with address " +
        "'$serverAddress' and port '$serverPort'"

    is ControlClientState.Stopped ->
        "connection to the server with address '$serverAddress' and port '$serverPort' " +
        "has benn stopped for the reason: ${error.toLog()}; Detailed message: ${cause.toLog()}"
}

private fun ControlServerError.toLog() = when (this) {
    ControlServerError.INIT_ERROR -> "initialization failed"
    ControlServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED -> "failed to accept new connections"
    ControlServerError.IO_ERROR -> "an I/O error has occurred"
    ControlServerError.SECURITY_ERROR -> "permission denied to accept a new connection"
    ControlServerError.UNKNOWN_ERROR -> "an unknown error has occurred"
}

private fun ControlClientError.toLog() =
    when (this) {
        ControlClientError.CONNECTION_FAILED -> "failed to connect to the server"
        ControlClientError.BREAK_CONNECTION -> "the connection to the server has been broken"
        ControlClientError.BAD_CONNECTION -> "bad connection to the server"
        ControlClientError.IO_ERROR -> "an I/O error has occurred when connecting to the server"
        ControlClientError.SECURITY_ERROR -> "permission denied to connect to the server"
        ControlClientError.UNKNOWN_ERROR ->
            "an unknown error has occurred when connecting to the server"
}

private fun Throwable.toLog() = when (localizedMessage) {
    null -> "<empty>"
    else -> localizedMessage
}

private fun Boolean.toLog() = when (this) {
    true -> "yes"
    false -> "no"
}
