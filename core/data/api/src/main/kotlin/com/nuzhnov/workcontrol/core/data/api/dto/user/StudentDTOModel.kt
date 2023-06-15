package com.nuzhnov.workcontrol.core.data.api.dto.user

import com.nuzhnov.workcontrol.core.data.api.dto.university.StudentDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupDTOModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      student: {
 *          id: 1,
 *          name: "Bakharov Malik",
 *          email: "mufti@mail.ru"
 *      },
 *
 *      group: {
 *          group: {
 *              id: 1,
 *              name: "AVT-919",
 *              course: 1
 *          },
 *
 *          department: {
 *              id: 1,
 *              name: "ACE"
 *          }
 *      }
 * }
 */
@JsonClass(generateAdapter = true)
data class StudentDTOModel(
    @Json(name = "student") val studentDTO: StudentDTO,
    @Json(name = "group") val groupDTOModel: GroupDTOModel
)
