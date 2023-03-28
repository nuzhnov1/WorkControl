package com.nuzhnov.workcontrol.core.visitcontrol.model

import org.joda.time.DateTime
import org.joda.time.Duration

data class Visitor(
    val id: Long,
    val isActive: Boolean,
    val lastVisit: DateTime?,
    val totalVisitDuration: Duration
)
