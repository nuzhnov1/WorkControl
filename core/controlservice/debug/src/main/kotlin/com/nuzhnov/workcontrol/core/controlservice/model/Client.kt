package com.nuzhnov.workcontrol.core.controlservice.model

import java.time.Duration
import java.time.LocalTime

data class Client(
    val id: Long,
    val isActive: Boolean,
    val lastVisit: LocalTime?,
    val totalVisitDuration: Duration
) : Comparable<Client> {
    override fun equals(other: Any?) = when (other) {
        is Client -> id == other.id
        else -> false
    }

    override fun hashCode() = id.hashCode()
    override fun compareTo(other: Client) = id.compareTo(other.id)
}
