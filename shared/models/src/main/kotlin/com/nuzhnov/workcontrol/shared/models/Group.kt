package com.nuzhnov.workcontrol.shared.models

data class Group(
    val id: Long,
    val name: String,
    val course: Byte,
    val faculty: Faculty
)
