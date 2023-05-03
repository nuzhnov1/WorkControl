package com.nuzhnov.workcontrol.core.preferences.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionPreferences(
    val id: Long,
    val login: String,
    @Json(name = "authorization_token") val authorizationToken: String,
    val role: Role
) {
    enum class Role {
        TEACHER,
        STUDENT
    }
}
