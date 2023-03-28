package com.nuzhnov.workcontrol.core.controlservice

import com.nuzhnov.workcontrol.core.controlservice.testcase.*
import com.nuzhnov.workcontrol.core.controlservice.model.Client
import com.nuzhnov.workcontrol.core.controlservice.mapper.toClient
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import java.util.*


private const val RANDOM_SEED = 0
private const val DELAY_AFTER_FINISH_MS = 1_000L

private val random = Random(RANDOM_SEED)
private val Client.isNotActive get() = !isActive


fun main() {
    ControlServiceApi.getDefaultControlServer().testCase(testData = testCaseData)
}

private fun ControlServiceApi.testCase(testData: Iterable<TestCaseData>) {
    testData.forEachIndexed { index, data ->
        println("Start test case #${index + 1}...")
        println("Test case info:\n$data")
        println("-".repeat(n = 80))

        runBlocking(Dispatchers.IO) {
            launch { startServerTask(testData = data) }
            launch { startClientTasks(testData = data) }
        }

        println("End testing.")
        println("Clients info:")
        logClientsInfo()
        println("-".repeat(n = 80))
    }

    println()
}

private suspend fun ControlServiceApi.startServerTask(testData: TestCaseData) = coroutineScope {
    launchServerStateObserver(service = this@startServerTask)
    launchClientsObserver(service = this@startServerTask)

    startServer(testData)
    delay(DELAY_AFTER_FINISH_MS)
    cancel()
}

private fun CoroutineScope.launchServerStateObserver(service: ControlServiceApi) = launch {
    var isCreated = false

    service.serverState.collect { state ->
        if (!isCreated) {
            log("Control Server: created.")
            isCreated = true
        }

        log("Control Server: ${state.toLog()}.")
    }
}

private fun CoroutineScope.launchClientsObserver(service: ControlServiceApi) = launch {
    var currentClientSet: SortedSet<Client> = sortedSetOf()

    service.clients
        .map { modelsSet -> modelsSet.map { model -> model.toClient() }.toSortedSet() }
        .collect { clientSet ->
            onUpdateClients(currentClientSet, clientSet)
            currentClientSet = clientSet
        }
}

private suspend fun ControlServiceApi.startServer(testData: TestCaseData) = runCatching {
    withTimeout(timeMillis = testData.serverWorkTimeMillis) {
        startServer(address = testData.serverAddress, port = testData.serverPort)
    }
}

private fun onUpdateClients(oldClientSet: SortedSet<Client>, newClientSet: SortedSet<Client>) {
    infix fun Boolean.but(other: Boolean) = this && other

    val newClients = newClientSet - oldClientSet

    newClients.forEach { client -> log("Control Server: new client with id = ${client.id}!") }
    oldClientSet.zip(newClientSet).forEach { (oldInfo, newInfo) ->
        if (oldInfo.isActive but newInfo.isNotActive) {
            log("Control Server: the client with id = ${newInfo.id} is not active now.")
        } else if (oldInfo.isNotActive but newInfo.isActive) {
            log("Control Server: the client with id = ${newInfo.id} is active now.")
        }
    }
}

private suspend fun startClientTasks(testData: TestCaseData) = coroutineScope {
    testData
        .generateClientTestData()
        .forEach { clientTestData -> startClientTask(testData, clientTestData) }
}

private fun TestCaseData.generateClientTestData() = buildList {
    repeat(clientsCount) {
        add(ClientTestData(
            id = random.nextLong(range = 0L..(clientsCount * 1000)),
            workTimeMillis = random.nextLong(
                range = clientMinWorkTimeMillis..clientMaxWorkTimeMillis
            ),
            delayTimeMillis = random.nextLong(
                range = clientMinAppearanceDelayTimeMillis..clientMaxAppearanceDelayTimeMillis
            ),
            interruptionsCount = random.nextInt(
                range = clientMinInterruptionCount..clientMaxInterruptionCount
            )
        ))
    }
}

private fun CoroutineScope.startClientTask(
    testData: TestCaseData,
    clientTestData: ClientTestData
) = launch {
    val controlClient = ControlServiceApi.getDefaultControlServer()

    launchClientStateObserver(service = controlClient, clientTestData = clientTestData)

    delay(timeMillis = clientTestData.delayTimeMillis)
    controlClient.startClient(testData, clientTestData)
    delay(DELAY_AFTER_FINISH_MS)
    cancel()
}

private fun CoroutineScope.launchClientStateObserver(
    service: ControlServiceApi,
    clientTestData: ClientTestData
) = launch {
    var isCreated = false

    service.clientState.collect { state ->
        if (!isCreated) {
            log("Client#${clientTestData.id}: created.")
            isCreated = true
        }

        log("Client#${clientTestData.id}: ${state.toLog()}.")
    }
}

private suspend fun ControlServiceApi.startClient(
    testData: TestCaseData,
    clientTestData: ClientTestData
) {
    val id = clientTestData.id
    val workTimeMillis = clientTestData.workTimeMillis
    val delayTimeMillis = clientTestData.delayTimeMillis
    val interruptionsCount = clientTestData.interruptionsCount

    val totalWorkTimeMillis = workTimeMillis - delayTimeMillis * interruptionsCount
    val partWorkTimeMillis = totalWorkTimeMillis / (interruptionsCount + 1)

    repeat(times = interruptionsCount + 1) {
        runCatching {
            withTimeout(timeMillis = partWorkTimeMillis) {
                startClient(
                    serverAddress = testData.serverAddress,
                    serverPort = testData.serverPort,
                    clientID = id
                )
            }
        }

        delay(delayTimeMillis)
    }
}

private fun ControlServiceApi.logClientsInfo() {
    println("Clients count: ${clients.value.size}.")
    clients.value
        .map { model -> model.toClient() }
        .forEach { client -> println(client.toLog()) }
}


private data class ClientTestData(
    val id: Long,
    val workTimeMillis: Long,
    val delayTimeMillis: Long,
    val interruptionsCount: Int
)
