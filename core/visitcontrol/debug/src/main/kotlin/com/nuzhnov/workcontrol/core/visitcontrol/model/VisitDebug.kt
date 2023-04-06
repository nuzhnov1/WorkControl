package com.nuzhnov.workcontrol.core.visitcontrol.model

import org.joda.time.DateTime
import org.joda.time.Duration

internal data class VisitDebug(
    val visitorID: VisitorID,
    val isActive: Boolean,
    val lastVisitTime: DateTime?,
    val totalVisitDuration: Duration
) : Comparable<VisitDebug> {
    override fun equals(other: Any?) = when (other) {
        is VisitDebug -> visitorID == other.visitorID
        else -> false
    }

    override fun hashCode() = visitorID.hashCode()
    override fun compareTo(other: VisitDebug) = visitorID.compareTo(other.visitorID)
}
