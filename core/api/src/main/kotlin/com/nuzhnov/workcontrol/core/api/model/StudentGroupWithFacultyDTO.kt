package com.nuzhnov.workcontrol.core.api.model

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
data class StudentGroupWithFacultyDTO(
    @Json(name = "group") val studentGroupDTO: StudentGroupDTO,
    @Json(name = "faculty") val facultyDTO: FacultyDTO
)
