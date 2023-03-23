package com.nuzhnov.workcontrol.core.controlservice.testcase

import java.net.InetAddress

internal data class TestCaseData(
    val serverWorkTimeMillis: Long,
    val clientsCount: Int,
    val serverAddress: InetAddress,
    val serverPort: Int,
    val clientMinWorkTimeMillis: Long,
    val clientMaxWorkTimeMillis: Long,
    val clientMinAppearanceDelayTimeMillis: Long,
    val clientMaxAppearanceDelayTimeMillis: Long,
    val clientMinInterruptionCount: Int,
    val clientMaxInterruptionCount: Int
)


internal val testCaseData = listOf(
    TestCaseData(
        serverWorkTimeMillis = 30_000L,
        clientsCount = 5,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        clientMinWorkTimeMillis = 1_000L,
        clientMaxWorkTimeMillis = 5_000L,
        clientMinAppearanceDelayTimeMillis = 2_000L,
        clientMaxAppearanceDelayTimeMillis = 3_000L,
        clientMinInterruptionCount = 0,
        clientMaxInterruptionCount = 0
    ),

    TestCaseData(
        serverWorkTimeMillis = 5_000L,
        clientsCount = 5,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        clientMinWorkTimeMillis = 1_000L,
        clientMaxWorkTimeMillis = 5_000L,
        clientMinAppearanceDelayTimeMillis = 1_000L,
        clientMaxAppearanceDelayTimeMillis = 2_000L,
        clientMinInterruptionCount = 0,
        clientMaxInterruptionCount = 0
    ),

    TestCaseData(
        serverWorkTimeMillis = 10_000L,
        clientsCount = 100,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        clientMinWorkTimeMillis = 1_000L,
        clientMaxWorkTimeMillis = 2_000L,
        clientMinAppearanceDelayTimeMillis = 0L,
        clientMaxAppearanceDelayTimeMillis = 0L,
        clientMinInterruptionCount = 0,
        clientMaxInterruptionCount = 0
    ),

    TestCaseData(
        serverWorkTimeMillis = 30_000L,
        clientsCount = 5,
        serverAddress = LOCALHOST,
        serverPort = DEFAULT_SERVER_PORT,
        clientMinWorkTimeMillis = 5_000L,
        clientMaxWorkTimeMillis = 10_000L,
        clientMinAppearanceDelayTimeMillis = 0L,
        clientMaxAppearanceDelayTimeMillis = 1L,
        clientMinInterruptionCount = 0,
        clientMaxInterruptionCount = 2
    )
)
