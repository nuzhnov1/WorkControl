package com.nuzhnov.workcontrol.shared.visitservice.domen.model

import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import org.joda.time.DateTime
import org.joda.time.Duration

data class Visitor(
    val id: VisitorID,
    val isActive: Boolean,
    val lastVisitTime: DateTime?,
    val totalVisitDuration: Duration
)
