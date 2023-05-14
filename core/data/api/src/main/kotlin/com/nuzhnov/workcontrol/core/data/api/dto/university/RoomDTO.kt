package com.nuzhnov.workcontrol.core.data.api.dto.university

import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      name: "7-220"
 * }
 */
@JsonClass(generateAdapter = true)
data class RoomDTO(
    val id: Long,
    val name: String
)
