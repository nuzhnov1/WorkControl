package com.nuzhnov.workcontrol.core.api.model

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
 *              id: 1,
 *              name: "AVT-919",
 *              course: 1
 *          },
 *
 *          {
 *              id: 2,
 *              name: "AVT-917",
 *              course: 1
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
    @Json(name = "room") val roomDTO: RoomWithBuildingDTO,
    val groups: List<StudentGroupWithFacultyDTO>,
    val theme: String,
    @Json(name = "visit_type") val visitType: Lesson.VisitType,
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "planned_duration") val plannedDuration: Double,
    @Json(name = "actual_duration") val actualDuration: Double
)
