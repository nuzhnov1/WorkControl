package com.nuzhnov.workcontrol.core.controlservice.model

import java.time.Duration
import java.time.LocalTime

// TODO: replace time classes with those that support api below 26
data class ClientApiModel(
    val id: Long,
    val isActive: Boolean,
    val lastVisit: LocalTime?,
    val totalVisitDuration: Duration
)
