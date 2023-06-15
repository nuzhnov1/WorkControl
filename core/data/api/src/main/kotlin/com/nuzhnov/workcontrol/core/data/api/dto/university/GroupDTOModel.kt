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
 *      department: {
 *          id: 1,
 *          name: "ACE"
 *      }
 * }
 */
@JsonClass(generateAdapter = true)
data class GroupDTOModel(
    @Json(name = "group") val groupDTO: GroupDTO,
    @Json(name = "department") val departmentDTO: DepartmentDTO
)
