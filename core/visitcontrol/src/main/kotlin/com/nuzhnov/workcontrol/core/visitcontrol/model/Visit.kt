package com.nuzhnov.workcontrol.core.visitcontrol.model

import org.joda.time.DateTime
import org.joda.time.Duration

data class Visit(
    val visitorID: Long,
    val isActive: Boolean,
    val lastVisitTime: DateTime?,
    val totalVisitDuration: Duration
)
