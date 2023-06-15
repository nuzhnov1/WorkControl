package com.nuzhnov.workcontrol.core.models

data class Group(
    val id: Long,
    val name: String,
    val course: Byte,
    val department: Department
)
