package com.nuzhnov.workcontrol.core.visitcontrol

import com.nuzhnov.workcontrol.core.visitcontrol.mapper.toVisitorDebug
import com.nuzhnov.workcontrol.core.visitcontrol.testcase.*
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorDebug
import com.nuzhnov.workcontrol.core.visitcontrol.server.Server
import com.nuzhnov.workcontrol.core.visitcontrol.client.Client
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
    Server.getDefaultServer().testCase(testData = testCaseData)
}

private fun Server.testCase(testData: Iterable<TestCaseData>) {
    testData.forEachIndexed { index, data ->
        println("Start test case #${index + 1}...")
        println("Test case info:\n$data")
        println("-".repeat(n = 80))

        runBlocking(Dispatchers.IO) {
            launch { startServerTask(testData = data) }
            launch { startVisitorsTasks(testData = data) }
        }

        println("End testing.")
        println("Visitors info:")
        logVisitorsInfo()
        println("-".repeat(n = 80))
    }

    println()
}

private suspend fun Server.startServerTask(testData: TestCaseData) = coroutineScope {
    launchServerStateObserver(server = this@startServerTask)
    launchVisitorsObserver(server = this@startServerTask)

    startServer(testData)
    delay(DELAY_AFTER_FINISH_MS)
    cancel()
}

private fun CoroutineScope.launchServerStateObserver(server: Server) = launch {
    var isCreated = false

    server.state.collect { state ->
        if (!isCreated) {
            log("Server: created.")
            isCreated = true
        }

        log("Server: ${state.toLog()}.")
    }
}

private fun CoroutineScope.launchVisitorsObserver(server: Server) = launch {
    var currentVisitors: SortedSet<VisitorDebug> = sortedSetOf()

    server.visitors
        .map { visitors -> visitors.map { visitor -> visitor.toVisitorDebug() }.toSortedSet() }
        .collect { visitors ->
            onUpdateVisitors(oldVisitorsSet = currentVisitors, newVisitorsSet = visitors)
            currentVisitors = visitors
        }
}

private suspend fun Server.startServer(testData: TestCaseData) = runCatching {
    withTimeout(timeMillis = testData.serverWorkTimeMillis) {
        start(address = testData.serverAddress, port = testData.serverPort)
    }
}

private fun onUpdateVisitors(
    oldVisitorsSet: SortedSet<VisitorDebug>,
    newVisitorsSet: SortedSet<VisitorDebug>
) {
    infix fun Boolean.but(other: Boolean) = this && other

    val newClients = newVisitorsSet - oldVisitorsSet

    newClients.forEach { client -> log("Server: new visitor with id = ${client.id}!") }
    oldVisitorsSet.zip(newVisitorsSet).forEach { (oldInfo, newInfo) ->
        if (oldInfo.isActive but newInfo.isNotActive) {
            log("Server: the visitor with id = ${newInfo.id} is not active now.")
        } else if (oldInfo.isNotActive but newInfo.isActive) {
            log("Server: the visitor with id = ${newInfo.id} is active now.")
        }
    }
}

private suspend fun startVisitorsTasks(testData: TestCaseData) = coroutineScope {
    testData
        .generateVisitorTestData()
        .forEach { visitorTestData -> startVisitorTask(testData, visitorTestData) }
}

private fun TestCaseData.generateVisitorTestData() = buildList {
    repeat(clientsCount) {
        add(VisitorTestData(
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

private fun CoroutineScope.startVisitorTask(
    testData: TestCaseData,
    visitorTestData: VisitorTestData
) = launch {
    val visitor = Client.getDefaultClient()

    launchClientStateObserver(client = visitor, visitorTestData = visitorTestData)

    delay(timeMillis = visitorTestData.delayTimeMillis)
    visitor.startVisitor(testData, visitorTestData)
    delay(DELAY_AFTER_FINISH_MS)
    cancel()
}

private fun CoroutineScope.launchClientStateObserver(
    client: Client,
    visitorTestData: VisitorTestData
) = launch {
    var isCreated = false

    client.state.collect { state ->
        if (!isCreated) {
            log("Visitor#${visitorTestData.id}: created.")
            isCreated = true
        }

        log("Visitor#${visitorTestData.id}: ${state.toLog()}.")
    }
}

private suspend fun Client.startVisitor(
    testData: TestCaseData,
    visitorTestData: VisitorTestData
) {
    val id = visitorTestData.id
    val workTimeMillis = visitorTestData.workTimeMillis
    val delayTimeMillis = visitorTestData.delayTimeMillis
    val interruptionsCount = visitorTestData.interruptionsCount

    val totalWorkTimeMillis = workTimeMillis - delayTimeMillis * interruptionsCount
    val partWorkTimeMillis = totalWorkTimeMillis / (interruptionsCount + 1)

    repeat(times = interruptionsCount + 1) {
        runCatching {
            withTimeout(timeMillis = partWorkTimeMillis) {
                start(
                    serverAddress = testData.serverAddress,
                    serverPort = testData.serverPort,
                    visitorID = id
                )
            }
        }

        delay(delayTimeMillis)
    }
}

private fun Server.logVisitorsInfo() {
    println("Visitors count: ${visitors.value.size}.")
    visitors.value
        .map { visitor -> visitor.toVisitorDebug() }
        .forEach { visitor -> println(visitor.toLog()) }
}


private data class VisitorTestData(
    val id: Long,
    val workTimeMillis: Long,
    val delayTimeMillis: Long,
    val interruptionsCount: Int
)
