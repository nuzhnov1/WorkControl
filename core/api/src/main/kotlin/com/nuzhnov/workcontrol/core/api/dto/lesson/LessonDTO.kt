package com.nuzhnov.workcontrol.core.api.dto.lesson

import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.api.dto.university.TeacherDTO
import com.nuzhnov.workcontrol.core.api.dto.university.RoomModelDTO
import com.nuzhnov.workcontrol.core.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.model.Lesson
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
 *              faculty: {
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
 *              faculty: {
 *                  id: 1,
 *                  name: "ACE"
 *              }
 *          }
 *      ],
 *
 *      theme: "Pointers",
 *      visit_type: "INTRAMURAL",
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
    @Json(name = "room") val roomModelDTO: RoomModelDTO,
    @Json(name = "groups") val groupModelDTOList: List<GroupModelDTO>,
    val theme: String,
    @Json(name = "visit_type") val visitType: Lesson.VisitType,
    @Json(name = "start_time") val startTime: Long?,
    @Json(name = "planned_duration") val plannedDuration: Double,
    @Json(name = "actual_duration") val actualDuration: Double?
)
