package com.nuzhnov.workcontrol.core.api.model

import com.nuzhnov.workcontrol.core.models.Lesson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      discipline_id: 1,
 *      teacher_id: 1,
 *      room_id: 1,
 *      theme: "Pointers",
 *      visit_type: "INTRAMURAL",
 *      start_time: 1928301923819203,
 *      planned_duration: 192810293810238.93208429034,
 *      actual_duration: 20394820934782.293084290348,
 *      participants: [
 *          {
 *              student_id: 1,
 *              total_visit_duration: 10293809128.20198340921384,
 *              is_marked: true,
 *              theory_assessment: 5,
 *              practice_assessment: 2,
 *              activity_assessment: null,
 *              prudence_assessment: null,
 *              creativity_assessment: null,
 *              preparation_assessment: 2,
 *              note: Cool guy
 *          },
 *
 *          {
 *              student_id: 2,
 *              total_visit_duration: 102983109238.9043834098,
 *              is_marked: false,
 *              theory_assessment: 1,
 *              practice_assessment: 2,
 *              activity_assessment: null,
 *              prudence_assessment: null,
 *              creativity_assessment: null,
 *              preparation_assessment: 2,
 *              note: null
 *          }
 *      ]
 * }
 */
@JsonClass(generateAdapter = true)
data class LessonPostedDTO(
    @Json(name = "discipline_id") val disciplineID: Long,
    @Json(name = "teacher_id") val teacherID: Long,
    @Json(name = "room_id") val roomID: Long,
    val theme: String,
    @Json(name = "visit_type") val visitType: Lesson.VisitType,
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "planned_duration") val plannedDuration: Double,
    @Json(name = "actual_duration") val actualDuration: Double,
    val participants: List<ParticipantPostedDTO>
)
