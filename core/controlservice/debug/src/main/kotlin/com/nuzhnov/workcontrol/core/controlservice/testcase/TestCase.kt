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
) {
    override fun toString() =
        "Server working time in seconds: ${serverWorkTimeMillis / 1000};\n" +
        "Clients count: $clientsCount;\n" +
        "Server address: $serverAddress;\n" +
        "Server port: $serverPort;\n" +
        "Client minimum working time in seconds: ${clientMinWorkTimeMillis / 1000};\n" +
        "Client maximum working time in seconds: ${clientMaxWorkTimeMillis / 1000};\n" +
        "Client minimum appearance time delay in seconds: ${clientMinAppearanceDelayTimeMillis / 1000};\n" +
        "Client maximum appearance time delay in seconds: ${clientMaxAppearanceDelayTimeMillis / 1000};\n" +
        "Client minimum interruption count: $clientMinInterruptionCount;\n" +
        "Client maximum interruption count: $clientMaxInterruptionCount;"
}


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
