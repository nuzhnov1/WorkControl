package com.nuzhnov.workcontrol.core.models

data class Student(
    val id: Long,
    val name: String,
    val email: String,
    val group: Group
)
