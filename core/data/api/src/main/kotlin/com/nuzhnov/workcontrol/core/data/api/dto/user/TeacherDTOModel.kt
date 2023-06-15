package com.nuzhnov.workcontrol.core.data.api.dto.user

import com.nuzhnov.workcontrol.core.data.api.dto.university.TeacherDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.DisciplineDTO
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
data class TeacherDTOModel(
    @Json(name = "teacher") val teacherDTO: TeacherDTO,
    val disciplineDTOList: List<DisciplineDTO>
)
