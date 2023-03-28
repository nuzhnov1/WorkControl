package com.nuzhnov.workcontrol.core.controlservice.model

import org.joda.time.DateTime
import org.joda.time.Duration

data class ClientApiModel(
    val id: Long,
    val isActive: Boolean,
    val lastVisit: DateTime?,
    val totalVisitDuration: Duration
)
