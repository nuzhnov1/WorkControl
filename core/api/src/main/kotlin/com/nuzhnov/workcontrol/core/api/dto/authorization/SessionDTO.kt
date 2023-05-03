package com.nuzhnov.workcontrol.core.api.dto.authorization

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      authorization_token: "dlfjvdfklvjdflvdkfv",
 *      role: "STUDENT"
 * }
 */
@JsonClass(generateAdapter = true)
data class SessionDTO(
    val id: Long,
    @Json(name = "authorization_token") val authorizationToken: String,
    val role: Role
) {
    enum class Role {
        TEACHER,
        STUDENT
    }
}
