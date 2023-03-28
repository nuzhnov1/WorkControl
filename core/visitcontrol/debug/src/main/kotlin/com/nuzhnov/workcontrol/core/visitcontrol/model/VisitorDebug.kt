package com.nuzhnov.workcontrol.core.visitcontrol.model

import org.joda.time.DateTime
import org.joda.time.Duration

internal data class VisitorDebug(
    val id: Long,
    val isActive: Boolean,
    val lastVisit: DateTime?,
    val totalVisitDuration: Duration
) : Comparable<VisitorDebug> {
    override fun equals(other: Any?) = when (other) {
        is VisitorDebug -> id == other.id
        else -> false
    }

    override fun hashCode() = id.hashCode()
    override fun compareTo(other: VisitorDebug) = id.compareTo(other.id)
}
