package com.nuzhnov.workcontrol.core.data.api.dto.university

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      group: {
 *          id: 1,
 *          name: "AVT-919",
 *          course: 1
 *      },
 *
 *      faculty: {
 *          id: 1,
 *          name: "ACE"
 *      }
 * }
 */
@JsonClass(generateAdapter = true)
data class GroupModelDTO(
    @Json(name = "group") val groupDTO: GroupDTO,
    @Json(name = "faculty") val facultyDTO: FacultyDTO
)
