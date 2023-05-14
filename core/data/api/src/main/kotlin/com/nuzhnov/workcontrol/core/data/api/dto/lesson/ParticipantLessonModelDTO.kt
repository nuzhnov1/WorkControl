package com.nuzhnov.workcontrol.core.data.api.dto.lesson

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      lesson: {
 *          id: 1,
 *
 *          discipline: {
 *              id: 1,
 *              name: "Programming"
 *          },
 *
 *          teacher: {
 *              id: 1,
 *              name: "Romanov",
 *              email: "romanov@mail.ru"
 *          }
 *
 *          room: {
 *              room: {
 *                  id: 1,
 *                  name: "7-220"
 *              },
 *
 *              building: {
 *                  id: 1,
 *                  name: "Seven building"
 *              }
 *          },
 *
 *          groups: [
 *              {
 *                  group: {
 *                      id: 1,
 *                      name: "AVT-919",
 *                      course: 1
 *                  },
 *
 *                  faculty: {
 *                      id: 1,
 *                      name: "ACE"
 *                  }
 *              },
 *
 *              {
 *                  group: {
 *                      id: 2,
 *                      name: "AVT-917",
 *                      course: 1
 *                  },
 *
 *                  faculty: {
 *                      id: 1,
 *                      name: "ACE"
 *                  }
 *              }
 *          ],
 *
 *          theme: "Pointers",
 *          type: "PRACTICE",
 *          start_time: 1394980193481,
 *          planned_duration: 1928129837123.93481,
 *          actual_duration: 98733498934857839457.993483847539
 *      }
 *
 *      total_visit_duration: 394875398457.03495834908,
 *      is_marked: true,
 *      theory_assessment: 10,
 *      practice_assessment: 8,
 *      activity_assessment: 5,
 *      prudence_assessment: null,
 *      creativity_assessment: null,
 *      preparation_assessment: null,
 *      note: "note"
 * }
 */
@JsonClass(generateAdapter = true)
data class ParticipantLessonModelDTO(
    @Json(name = "lesson") val lessonDTO: LessonDTO,
    @Json(name = "total_visit_duration") val totalVisitDuration: Double,
    @Json(name = "is_marked") val isMarked: Boolean,
    @Json(name = "theory_assessment") val theoryAssessment: Byte?,
    @Json(name = "practice_assessment") val practiceAssessment: Byte?,
    @Json(name = "activity_assessment") val activityAssessment: Byte?,
    @Json(name = "prudence_assessment") val prudenceAssessment: Byte?,
    @Json(name = "creativity_assessment") val creativityAssessment: Byte?,
    @Json(name = "preparation_assessment") val preparationAssessment: Byte?,
    val note: String?
)
