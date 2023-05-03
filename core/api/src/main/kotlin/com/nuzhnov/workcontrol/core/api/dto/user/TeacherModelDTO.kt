package com.nuzhnov.workcontrol.core.api.dto.user

import com.nuzhnov.workcontrol.core.api.dto.university.TeacherDTO
import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      teacher: {
 *          id: 1,
 *          name: "Romanov Evgeni",
 *          email: "romanov@mail.ru"
 *      },
 *
 *      disciplines: [
 *          {
 *              id: 1,
 *              name: "Programming"
 *          },
 *
 *          {
 *              id: 2,
 *              name: "Program engineering"
 *          }
 *      ]
 * }
 */
@JsonClass(generateAdapter = true)
data class TeacherModelDTO(
    @Json(name = "teacher") val teacherDTO: TeacherDTO,
    val disciplines: List<DisciplineDTO>
)
