package com.nuzhnov.workcontrol.common.visitcontrol

import com.nuzhnov.workcontrol.common.visitcontrol.mapper.toVisitDebug
import com.nuzhnov.workcontrol.common.visitcontrol.testcase.*
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitDebug
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServer
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.Visitor
import kotlin.random.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*


private const val RANDOM_SEED = 0
private val RANDOM = Random(RANDOM_SEED)

private val VisitDebug.isNotActive get() = !isActive


fun main() {
    ControlServer.getDefaultControlServer().test(
        testCaseType4
    )
}

private fun ControlServer.test(vararg testCase: TestCase) {
    testCase.forEachIndexed { index, data ->
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

private suspend fun ControlServer.startControlTask(testData: TestCase) = coroutineScope {
    val serverStateObserverJob = launchServerStateObserver(controlServer = this@startControlTask)
    val visitorsObserverJob = launchVisitorsObserver(controlServer = this@startControlTask)
    val terminatorJob = launchTerminatorJob(controlServer = this@startControlTask, testData)

    startControl(testData)
    serverStateObserverJob.cancelAndJoin()
    visitorsObserverJob.cancelAndJoin()
    terminatorJob.cancelAndJoin()
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
        .onCompletion { currentVisits.sortedBy { visit -> visit.totalVisitDuration }.log() }
        .collect()
}

private fun CoroutineScope.launchTerminatorJob(
    controlServer: ControlServer,
    testData: TestCase
) = launch {
    var remainingDisconnections = testData.disconnectionsNumber
    var oldActiveVisits: Set<Visit> = setOf()

    controlServer.visits.collect { visitSet ->
        val activeVisits = visitSet.filter { visit -> visit.isActive }.toSet()

        if (remainingDisconnections > 0) {
            oldActiveVisits.randomOrNull()?.apply {
                log("ControlServer: disconnect the visitor with id = $visitorID.")

                controlServer.disconnectVisitor(visitorID)
                remainingDisconnections--
            }
        }

        oldActiveVisits = activeVisits
    }
}

private suspend fun ControlServer.startControl(testData: TestCase) = runCatching {
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

    log("ControlServer: visits updated.")
    newVisits.forEach { visit -> log("ControlServer: new visitor with id = ${visit.visitorID}!") }
    oldVisitsSet.zip(newVisitsSet).forEach { (oldInfo, newInfo) ->
        if (oldInfo.isActive but newInfo.isNotActive) {
            log("ControlServer: the visitor with id = ${newInfo.visitorID} is not active now.")
        } else if (oldInfo.isNotActive but newInfo.isActive) {
            log("ControlServer: the visitor with id = ${newInfo.visitorID} is active now.")
        }
    }
}

private suspend fun startVisitorsTasks(testData: TestCase) = coroutineScope {
    testData
        .generateVisitorTestData()
        .forEach { visitorTestData -> startVisitorTask(testData, visitorTestData) }
}

private fun TestCase.generateVisitorTestData() = buildList {
    repeat(visitorsCount) {
        add(VisitorTestData(
            id = RANDOM.nextLong(range = 0L..(visitorsCount * 1000)),
            workTimeMillis = RANDOM.nextLong(
                range = minVisitTimeMillis..maxVisitTimeMillis
            ),
            delayTimeMillis = RANDOM.nextLong(
                range = visitorMinAppearanceDelayTimeMillis..visitorMaxAppearanceDelayTimeMillis
            ),
            interruptionsCount = RANDOM.nextInt(
                range = visitorMinInterruptionCount..visitorMaxInterruptionCount
            )
        ))
    }
}

private fun CoroutineScope.startVisitorTask(
    testData: TestCase,
    visitorTestData: VisitorTestData
) = launch {
    val visitor = Visitor.getDefaultVisitor()
    val visitorStateObserverJob = launchVisitorStateObserver(visitor, visitorTestData)

    delay(timeMillis = visitorTestData.delayTimeMillis)
    visitor.startVisitor(testData, visitorTestData)
    visitorStateObserverJob.cancelAndJoin()
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
    testData: TestCase,
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
                startVisit(
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
