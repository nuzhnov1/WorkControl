package com.nuzhnov.workcontrol.core.model

data class Group(
    val id: Long,
    val name: String,
    val course: Byte,
    val faculty: Faculty
)
