package com.nuzhnov.workcontrol.core.api.model

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
data class StudentGroupDTO(
    val id: Long,
    val name: String,
    val course: Byte
)
