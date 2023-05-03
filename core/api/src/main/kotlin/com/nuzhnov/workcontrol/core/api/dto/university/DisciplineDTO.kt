package com.nuzhnov.workcontrol.core.api.dto.university

import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      name: "Programming"
 * }
 */
@JsonClass(generateAdapter = true)
data class DisciplineDTO(
    val id: Long,
    val name: String
)
