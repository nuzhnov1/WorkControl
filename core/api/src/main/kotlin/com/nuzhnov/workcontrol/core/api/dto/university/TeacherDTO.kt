package com.nuzhnov.workcontrol.core.api.dto.university

import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      name: "Romanov Evgeni",
 *      email: "romanov@mail.ru"
 * }
 */
@JsonClass(generateAdapter = true)
data class TeacherDTO(
    val id: Long,
    val name: String,
    val email: String
)
