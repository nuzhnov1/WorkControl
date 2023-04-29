package com.nuzhnov.workcontrol.core.api.model

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
