package com.nuzhnov.controlservice.data.model

import java.time.Duration
import java.time.LocalTime

// TODO: replace time classes with those that support api below 26
data class Client(
    val uniqueID: Long,
    val isActive: Boolean,
    val lastVisit: LocalTime?,
    val totalVisitDuration: Duration
)
