package com.nuzhnov.workcontrol.core.visitcontrol

import com.nuzhnov.workcontrol.core.visitcontrol.mapper.toVisitDebug
import com.nuzhnov.workcontrol.core.visitcontrol.testcase.*
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitDebug
import com.nuzhnov.workcontrol.core.visitcontrol.control.ControlServer
import com.nuzhnov.workcontrol.core.visitcontrol.visitor.Visitor
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import java.util.*


private const val RANDOM_SEED = 0
private const val DELAY_AFTER_FINISH_MS = 1_000L

private val random = Random(RANDOM_SEED)
private val VisitDebug.isNotActive get() = !isActive


fun main() {
    ControlServer.getDefaultControlServer().testCase(testData = testCaseData)
}

private fun ControlServer.testCase(testData: Iterable<TestCaseData>) {
    testData.forEachIndexed { index, data ->
        println("Start test case #${index + 1}...")
        println("Test case info:\n$data")
        println("-".repeat(n = 80))

        runBlocking(Dispatchers.IO) {
            launch { startControlTask(testData = data) }
            launch { startVisitorsTasks(testData = data) }
        }

        println("End testing.")
        clearVisits()
        println("-".repeat(n = 80))
    }

    println()
}

private suspend fun ControlServer.startControlTask(testData: TestCaseData) = coroutineScope {
    val serverStateObserverJob = launchServerStateObserver(controlServer = this@startControlTask)
    val visitorsObserverJob = launchVisitorsObserver(controlServer = this@startControlTask)

    startControl(testData)
    serverStateObserverJob.cancel()
    visitorsObserverJob.cancel()
    delay(DELAY_AFTER_FINISH_MS)
}

private fun CoroutineScope.launchServerStateObserver(controlServer: ControlServer) = launch {
    var isCreated = false

    controlServer.state.collect { state ->
        if (!isCreated) {
            log("ControlServer: created.")
            isCreated = true
        }

        log("ControlServer: ${state.toLog()}.")
    }
}

private fun CoroutineScope.launchVisitorsObserver(controlServer: ControlServer) = launch {
    var currentVisits: SortedSet<VisitDebug> = sortedSetOf()

    controlServer.visits
        .map { visits -> visits.map { visit -> visit.toVisitDebug() }.toSortedSet() }
        .onEach { visits ->
            onUpdateVisits(oldVisitsSet = currentVisits, newVisitsSet = visits)
            currentVisits = visits
        }
        .onCompletion { currentVisits.log() }
        .collect()
}

private suspend fun ControlServer.startControl(testData: TestCaseData) = runCatching {
    withTimeout(timeMillis = testData.serverWorkTimeMillis) {
        start(testData.serverAddress, testData.serverPort)
    }
}

private fun onUpdateVisits(
    oldVisitsSet: SortedSet<VisitDebug>,
    newVisitsSet: SortedSet<VisitDebug>
) {
    infix fun Boolean.but(other: Boolean) = this && other

    val newVisits = newVisitsSet - oldVisitsSet

    newVisits.forEach { visit -> log("ControlServer: new visitor with id = ${visit.visitorID}!") }
    oldVisitsSet.zip(newVisitsSet).forEach { (oldInfo, newInfo) ->
        if (oldInfo.isActive but newInfo.isNotActive) {
            log("ControlServer: the visitor with id = ${newInfo.visitorID} is not active now.")
        } else if (oldInfo.isNotActive but newInfo.isActive) {
            log("ControlServer: the visitor with id = ${newInfo.visitorID} is active now.")
        }
    }
}

private suspend fun startVisitorsTasks(testData: TestCaseData) = coroutineScope {
    testData
        .generateVisitorTestData()
        .forEach { visitorTestData -> startVisitorTask(testData, visitorTestData) }
}

private fun TestCaseData.generateVisitorTestData() = buildList {
    repeat(visitorsCount) {
        add(VisitorTestData(
            id = random.nextLong(range = 0L..(visitorsCount * 1000)),
            workTimeMillis = random.nextLong(
                range = minVisitTimeMillis..maxVisitTimeMillis
            ),
            delayTimeMillis = random.nextLong(
                range = visitorMinAppearanceDelayTimeMillis..visitorMaxAppearanceDelayTimeMillis
            ),
            interruptionsCount = random.nextInt(
                range = visitorMinInterruptionCount..visitorMaxInterruptionCount
            )
        ))
    }
}

private fun CoroutineScope.startVisitorTask(
    testData: TestCaseData,
    visitorTestData: VisitorTestData
) = launch {
    val visitor = Visitor.getDefaultVisitor()
    val visitorStateObserverJob = launchVisitorStateObserver(visitor, visitorTestData)

    delay(timeMillis = visitorTestData.delayTimeMillis)
    visitor.startVisitor(testData, visitorTestData)
    visitorStateObserverJob.cancel()
    delay(DELAY_AFTER_FINISH_MS)
}

private fun CoroutineScope.launchVisitorStateObserver(
    visitor: Visitor,
    visitorTestData: VisitorTestData
) = launch {
    var isCreated = false

    visitor.state.collect { state ->
        if (!isCreated) {
            log("Visitor#${visitorTestData.id}: created.")
            isCreated = true
        }

        log("Visitor#${visitorTestData.id}: ${state.toLog()}.")
    }
}

private suspend fun Visitor.startVisitor(
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


private data class VisitorTestData(
    val id: VisitorID,
    val workTimeMillis: Long,
    val delayTimeMillis: Long,
    val interruptionsCount: Int
)
