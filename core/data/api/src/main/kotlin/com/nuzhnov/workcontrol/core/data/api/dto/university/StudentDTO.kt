package com.nuzhnov.workcontrol.core.data.api.dto.university

import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *      name: "Bakharov Malik",
 *      email: "mufti@mail.ru"
 * }
 */
@JsonClass(generateAdapter = true)
data class StudentDTO(
    val id: Long,
    val name: String,
    val email: String
)
