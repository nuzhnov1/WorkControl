package com.nuzhnov.workcontrol.shared.models

data class Student(
    val id: Long,
    val name: String,
    val email: String,
    val group: Group
)
