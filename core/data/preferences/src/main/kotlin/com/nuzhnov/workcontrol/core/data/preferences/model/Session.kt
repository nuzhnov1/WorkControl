package com.nuzhnov.workcontrol.core.data.preferences.model

import com.nuzhnov.workcontrol.core.model.Role
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Session(
    val id: Long,
    @Json(name = "authorization_token") val authorizationToken: String,
    val role: Role
)
