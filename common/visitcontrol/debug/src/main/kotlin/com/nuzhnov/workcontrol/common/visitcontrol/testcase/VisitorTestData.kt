package com.nuzhnov.workcontrol.common.visitcontrol.testcase

import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID

internal data class VisitorTestData(
    val id: VisitorID,
    val workTimeMillis: Long,
    val delayTimeMillis: Long,
    val interruptionsCount: Int
) {
    override fun toString() =
        "Visitor#$id test data:\n" +
        "\tWork time in millis: $workTimeMillis;\n" +
        "\tDelay time in millis: $delayTimeMillis;\n" +
        "\tInterruptions count: $interruptionsCount;\n"
}
