package com.nuzhnov.workcontrol.core.api.model

import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      name: "Seven building"
 * }
 */
@JsonClass(generateAdapter = true)
data class BuildingDTO(
    val id: Long,
    val name: String
)
