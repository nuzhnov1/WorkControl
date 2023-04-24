package com.nuzhnov.workcontrol.common.visitcontrol.model

import com.nuzhnov.workcontrol.common.visitcontrol.annotation.Unique
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan

data class Visit(
    @Unique val visitorID: VisitorID,
    val isActive: Boolean,
    val lastVisit: DateTimeTz?,
    val totalVisitDuration: TimeSpan
)
