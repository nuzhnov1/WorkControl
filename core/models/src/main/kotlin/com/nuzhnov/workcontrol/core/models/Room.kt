package com.nuzhnov.workcontrol.core.models

data class Room(
    val id: Long,
    val name: String,
    val building: Building
)
