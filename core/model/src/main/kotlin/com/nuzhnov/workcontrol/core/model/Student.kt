package com.nuzhnov.workcontrol.core.model

data class Student(
    val id: Long,
    val name: String,
    val email: String,
    val group: Group
)
