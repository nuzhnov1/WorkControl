package com.nuzhnov.workcontrol.core.model

data class Session(
    val id: Long,
    val login: String,
    val authorizationToken: String,
    val role: Role
) {
    enum class Role {
        TEACHER,
        STUDENT
    }
}
