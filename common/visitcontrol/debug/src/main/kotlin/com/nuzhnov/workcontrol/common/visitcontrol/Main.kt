package com.nuzhnov.workcontrol.common.visitcontrol

import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServer
import com.nuzhnov.workcontrol.common.visitcontrol.mapper.toVisitDebug
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitDebug
import com.nuzhnov.workcontrol.common.visitcontrol.testcase.*
import com.nuzhnov.workcontrol.common.visitcontrol.visitor.Visitor
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import java.util.*


private const val DELAY_AFTER_COMPLETE_MS = 1_000L
private val RANDOM = Random(System.currentTimeMillis())

private val VisitDebug.isNotActive get() = !isActive


fun main() {
    ControlServer.getDefaultControlServer().test(
        testCaseType1,
        testCaseType2,
        testCaseType3,
        testCaseType4,
        testCaseType5,
        testCaseType6
    )
}

private fun ControlServer.test(vararg testCase: TestCase) =
    testCase.forEachIndexed { index, testData ->
        val visitorTestDataList = testData.generateVisitorTestData()

        println("Test case #${index + 1}...")
        print("Test case info:\n$testData")
        println("-".repeat(n = 80))
        println("Visitors test data:")
        println("-".repeat(n = 80))
        visitorTestDataList
            .sortedBy { visitorTestData -> visitorTestData.id }
            .forEach { visitorTestData -> print("$visitorTestData") }
        println("-".repeat(n = 80))

        println("Logs:")
        println("-".repeat(n = 80))

        runBlocking(Dispatchers.IO) {
            launch { startControlTask(testData = testData) }
            launch { startVisitorsTasks(testData = testData, visitorTestDataList) }
        }

        println("End testing.")
        clearVisits()
        println("-".repeat(n = 80))
        println()
    }

private suspend fun ControlServer.startControlTask(testData: TestCase) = coroutineScope {
    val serverStateObserverJob = launchServerStateObserver(controlServer = this@startControlTask)
    val visitorsObserverJob = launchVisitorsObserver(controlServer = this@startControlTask)
    val terminatorJob = launchTerminatorJob(controlServer = this@startControlTask, testData)

    startControl(testData)
    delay(DELAY_AFTER_COMPLETE_MS)
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
        .onCompletion { currentVisits.sortedBy { visit -> visit.visitorID }.log() }
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
    val newVisits = newVisitsSet - oldVisitsSet

    log("ControlServer: visits updated.")
    newVisits.forEach { visit -> log("ControlServer: new visitor with id = ${visit.visitorID}!") }
    oldVisitsSet.zip(newVisitsSet).forEach { (oldInfo, newInfo) ->
        if (oldInfo.isActive && newInfo.isNotActive) {
            log("ControlServer: the visitor with id = ${newInfo.visitorID} is not active now.")
        } else if (oldInfo.isNotActive && newInfo.isActive) {
            log("ControlServer: the visitor with id = ${newInfo.visitorID} is active now.")
        }
    }
}

private suspend fun startVisitorsTasks(
    testData: TestCase,
    visitorTestDataList: List<VisitorTestData>
) = coroutineScope {
    visitorTestDataList.forEach { visitorTestData ->
        delay(timeMillis = visitorTestData.delayTimeMillis)
        startVisitorTask(testData, visitorTestData)
    }
}

private fun TestCase.generateVisitorTestData() = buildList {
    repeat(visitorsCount) {
        add(
            VisitorTestData(
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
            )
        )
    }
}

private fun CoroutineScope.startVisitorTask(
    testData: TestCase,
    visitorTestData: VisitorTestData
) = launch {
    val visitor = Visitor.getDefaultVisitor()
    val visitorStateObserverJob = launchVisitorStateObserver(visitor, visitorTestData)

    visitor.startVisitor(testData, visitorTestData)
    delay(DELAY_AFTER_COMPLETE_MS)
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



