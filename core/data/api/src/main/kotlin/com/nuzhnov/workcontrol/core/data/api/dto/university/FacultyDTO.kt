package com.nuzhnov.workcontrol.core.data.api.dto.university

import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      name: "ACE"
 * }
 */
@JsonClass(generateAdapter = true)
data class FacultyDTO(
    val id: Long,
    val name: String
)
