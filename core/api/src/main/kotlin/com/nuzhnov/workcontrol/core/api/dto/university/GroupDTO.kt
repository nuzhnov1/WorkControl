package com.nuzhnov.workcontrol.core.api.dto.university

import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      name: "AVT-919",
 *      course: 1
 * }
 */
@JsonClass(generateAdapter = true)
data class GroupDTO(
    val id: Long,
    val name: String,
    val course: Byte
)
