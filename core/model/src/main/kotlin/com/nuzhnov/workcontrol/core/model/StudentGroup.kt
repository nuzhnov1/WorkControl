package com.nuzhnov.workcontrol.core.model

data class StudentGroup(
    val id: Long,
    val name: String,
    val course: Byte,
    val faculty: Faculty
)
