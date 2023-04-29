package com.nuzhnov.workcontrol.core.models

data class StudentGroup(
    val id: Long,
    val name: String,
    val course: Byte,
    val faculty: Faculty
)
