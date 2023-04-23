package com.nuzhnov.workcontrol.shared.models

data class Room(
    val id: Long,
    val name: String,
    val building: Building
)
