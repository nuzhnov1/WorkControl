package com.nuzhnov.workcontrol.shared.models.user

data class User(
    val id: Long,
    val login: String,
    val passwordHash: String,
    val name: String,
    val email: String,
    val role: Role,
    val isAuthorized: Boolean
)
