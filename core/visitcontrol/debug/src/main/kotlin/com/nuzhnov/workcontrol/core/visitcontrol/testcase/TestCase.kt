package com.nuzhnov.workcontrol.core.visitcontrol.testcase

import java.net.InetAddress

internal data class TestCaseData(
    val serverWorkTimeMillis: Long,
    val visitorsCount: Int,
    val serverAddress: InetAddress,
    val serverPort: Int,
    val minVisitTimeMillis: Long,
    val maxVisitTimeMillis: Long,
    val visitorMinAppearanceDelayTimeMillis: Long,
    val visitorMaxAppearanceDelayTimeMillis: Long,
    val visitorMinInterruptionCount: Int,
    val visitorMaxInterruptionCount: Int
) {
    override fun toString() =
        "ControlServer working time in seconds: ${serverWorkTimeMillis / MILLIS_IN_SECOND};\n" +
        "Visitors count: $visitorsCount;\n" +
        "ControlServer address: $serverAddress;\n" +
        "ControlServer port: $serverPort;\n" +
        "Visitor minimum working time in seconds: " +
            "${minVisitTimeMillis / MILLIS_IN_SECOND};\n" +
        "Visitor maximum working time in seconds: " +
            "${maxVisitTimeMillis / MILLIS_IN_SECOND};\n" +
        "Visitor minimum appearance time delay in seconds: " +
            "${visitorMinAppearanceDelayTimeMillis / MILLIS_IN_SECOND};\n" +
        "Visitor maximum appearance time delay in seconds: " +
            "${visitorMaxAppearanceDelayTimeMillis / MILLIS_IN_SECOND};\n" +
        "Visitor minimum interruption count: $visitorMinInterruptionCount;\n" +
        "Visitor maximum interruption count: $visitorMaxInterruptionCount;"


    companion object {
        const val MILLIS_IN_SECOND = 1000
    }
}


internal val testCaseData = listOf(
    TestCaseData(
        serverWorkTimeMillis = 30_000L,
        visitorsCount = 5,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        minVisitTimeMillis = 1_000L,
        maxVisitTimeMillis = 5_000L,
        visitorMinAppearanceDelayTimeMillis = 2_000L,
        visitorMaxAppearanceDelayTimeMillis = 3_000L,
        visitorMinInterruptionCount = 0,
        visitorMaxInterruptionCount = 0
    ),

    TestCaseData(
        serverWorkTimeMillis = 5_000L,
        visitorsCount = 5,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        minVisitTimeMillis = 1_000L,
        maxVisitTimeMillis = 5_000L,
        visitorMinAppearanceDelayTimeMillis = 1_000L,
        visitorMaxAppearanceDelayTimeMillis = 2_000L,
        visitorMinInterruptionCount = 0,
        visitorMaxInterruptionCount = 0
    ),

    TestCaseData(
        serverWorkTimeMillis = 10_000L,
        visitorsCount = 100,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        minVisitTimeMillis = 1_000L,
        maxVisitTimeMillis = 2_000L,
        visitorMinAppearanceDelayTimeMillis = 0L,
        visitorMaxAppearanceDelayTimeMillis = 0L,
        visitorMinInterruptionCount = 0,
        visitorMaxInterruptionCount = 0
    ),

    TestCaseData(
        serverWorkTimeMillis = 30_000L,
        visitorsCount = 5,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        minVisitTimeMillis = 5_000L,
        maxVisitTimeMillis = 10_000L,
        visitorMinAppearanceDelayTimeMillis = 0L,
        visitorMaxAppearanceDelayTimeMillis = 1L,
        visitorMinInterruptionCount = 0,
        visitorMaxInterruptionCount = 2
    )
)
