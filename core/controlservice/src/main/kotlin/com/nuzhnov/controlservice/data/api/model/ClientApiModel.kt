package com.nuzhnov.controlservice.data.api.model

import java.time.Duration
import java.time.LocalTime

// TODO: replace time classes with those that support api below 26
data class ClientApiModel(
    val uniqueID: Long,
    val isActive: Boolean,
    val lastVisit: LocalTime?,
    val totalVisitDuration: Duration
) {
    override fun equals(other: Any?) = when (other) {
        is ClientApiModel -> uniqueID == other.uniqueID
        else -> false
    }

    override fun hashCode() = uniqueID.hashCode()
}
