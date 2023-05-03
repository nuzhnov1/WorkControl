package com.nuzhnov.workcontrol.common.visitcontrol.model

import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan

internal data class VisitDebug(
    val visitorID: VisitorID,
    val isActive: Boolean,
    val lastVisitTime: DateTimeTz?,
    val totalVisitDuration: TimeSpan
) : Comparable<VisitDebug> {
    override fun equals(other: Any?) = when (other) {
        is VisitDebug   -> visitorID == other.visitorID
        else            -> false
    }

    override fun hashCode() = visitorID.hashCode()
    override fun compareTo(other: VisitDebug) = visitorID.compareTo(other.visitorID)
}
