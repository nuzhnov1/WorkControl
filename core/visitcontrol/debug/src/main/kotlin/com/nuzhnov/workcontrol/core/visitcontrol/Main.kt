package com.nuzhnov.workcontrol.core.visitcontrol

import com.nuzhnov.workcontrol.core.visitcontrol.mapper.toVisitorDebug
import com.nuzhnov.workcontrol.core.visitcontrol.testcase.*
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorDebug
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import java.util.*


private const val RANDOM_SEED = 0
private const val DELAY_AFTER_FINISH_MS = 1_000L

private val random = Random(RANDOM_SEED)
private val VisitorDebug.isNotActive get() = !isActive


fun main() {
    VisitControlService.getDefaultControlServer().testCase(testData = testCaseData)
}

private fun VisitControlService.testCase(testData: Iterable<TestCaseData>) {
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

private suspend fun VisitControlService.startServerTask(testData: TestCaseData) = coroutineScope {
    launchServerStateObserver(service = this@startServerTask)
    launchVisitorsObserver(service = this@startServerTask)

    startServer(testData)
    delay(DELAY_AFTER_FINISH_MS)
    cancel()
}

private fun CoroutineScope.launchServerStateObserver(service: VisitControlService) = launch {
    var isCreated = false

    service.controlServerState.collect { state ->
        if (!isCreated) {
            log("Control Server: created.")
            isCreated = true
        }

        log("Control Server: ${state.toLog()}.")
    }
}

private fun CoroutineScope.launchVisitorsObserver(service: VisitControlService) = launch {
    var currentVisitors: SortedSet<VisitorDebug> = sortedSetOf()

    service.visitors
        .map { visitors -> visitors.map { visitor -> visitor.toVisitorDebug() }.toSortedSet() }
        .collect { visitors ->
            onUpdateClients(oldVisitorsSet = currentVisitors, newVisitorsSet = visitors)
            currentVisitors = visitors
        }
}

private suspend fun VisitControlService.startServer(testData: TestCaseData) = runCatching {
    withTimeout(timeMillis = testData.serverWorkTimeMillis) {
        startControlServer(address = testData.serverAddress, port = testData.serverPort)
    }
}

private fun onUpdateClients(
    oldVisitorsSet: SortedSet<VisitorDebug>,
    newVisitorsSet: SortedSet<VisitorDebug>
) {
    infix fun Boolean.but(other: Boolean) = this && other

    val newClients = newVisitorsSet - oldVisitorsSet

    newClients.forEach { client -> log("Control Server: new visitor with id = ${client.id}!") }
    oldVisitorsSet.zip(newVisitorsSet).forEach { (oldInfo, newInfo) ->
        if (oldInfo.isActive but newInfo.isNotActive) {
            log("Control Server: the visitor with id = ${newInfo.id} is not active now.")
        } else if (oldInfo.isNotActive but newInfo.isActive) {
            log("Control Server: the visitor with id = ${newInfo.id} is active now.")
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
            visitorID = random.nextLong(range = 0L..(clientsCount * 1000)),
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
    val client = VisitControlService.getDefaultControlServer()

    launchClientStateObserver(service = client, clientTestData = clientTestData)

    delay(timeMillis = clientTestData.delayTimeMillis)
    client.startClient(testData, clientTestData)
    delay(DELAY_AFTER_FINISH_MS)
    cancel()
}

private fun CoroutineScope.launchClientStateObserver(
    service: VisitControlService,
    clientTestData: ClientTestData
) = launch {
    var isCreated = false

    service.clientState.collect { state ->
        if (!isCreated) {
            log("Visitor#${clientTestData.visitorID}: created.")
            isCreated = true
        }

        log("Visitor#${clientTestData.visitorID}: ${state.toLog()}.")
    }
}

private suspend fun VisitControlService.startClient(
    testData: TestCaseData,
    clientTestData: ClientTestData
) {
    val id = clientTestData.visitorID
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
                    visitorID = id
                )
            }
        }

        delay(delayTimeMillis)
    }
}

private fun VisitControlService.logClientsInfo() {
    println("Visitors count: ${visitors.value.size}.")
    visitors.value
        .map { visitor -> visitor.toVisitorDebug() }
        .forEach { visitor -> println(visitor.toLog()) }
}


private data class ClientTestData(
    val visitorID: Long,
    val workTimeMillis: Long,
    val delayTimeMillis: Long,
    val interruptionsCount: Int
)
