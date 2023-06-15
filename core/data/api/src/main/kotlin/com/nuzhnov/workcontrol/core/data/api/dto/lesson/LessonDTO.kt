package com.nuzhnov.workcontrol.core.data.api.dto.lesson

import com.nuzhnov.workcontrol.core.data.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.TeacherDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.RoomDTOModel
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupDTOModel
import com.nuzhnov.workcontrol.core.models.Lesson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      id: 1,
 *
 *      discipline: {
 *          id: 1,
 *          name: "Programming"
 *      },
 *
 *      teacher: {
 *          id: 1,
 *          name: "Romanov",
 *          email: "romanov@mail.ru"
 *      }
 *
 *      room: {
 *          room: {
 *              id: 1,
 *              name: "7-220"
 *          },
 *
 *          building: {
 *              id: 1,
 *              name: "Seven building"
 *          }
 *      },
 *
 *      groups: [
 *          {
 *              group: {
 *                  id: 1,
 *                  name: "AVT-919",
 *                  course: 1
 *              },
 *
 *              department: {
 *                  id: 1,
 *                  name: "ACE"
 *              }
 *          },
 *
 *          {
 *              group: {
 *                  id: 2,
 *                  name: "AVT-917",
 *                  course: 1
 *              },
 *
 *              department: {
 *                  id: 1,
 *                  name: "ACE"
 *              }
 *          }
 *      ],
 *
 *      theme: "Pointers",
 *      type: "PRACTICE",
 *      start_time: 1394980193481,
 *      planned_duration: 1928129837123.93481,
 *      actual_duration: 98733498934857839457.993483847539
 * }
 */
@JsonClass(generateAdapter = true)
data class LessonDTO(
    val id: Long,
    @Json(name = "discipline") val disciplineDTO: DisciplineDTO,
    @Json(name = "teacher") val teacherDTO: TeacherDTO,
    @Json(name = "room") val roomDTOModel: RoomDTOModel,
    @Json(name = "groups") val groupDTOModelList: List<GroupDTOModel>,
    val theme: String,
    val type: Lesson.Type,
    @Json(name = "start_time") val startTime: Long?,
    @Json(name = "planned_duration") val plannedDuration: Double,
    @Json(name = "actual_duration") val actualDuration: Double?
)
