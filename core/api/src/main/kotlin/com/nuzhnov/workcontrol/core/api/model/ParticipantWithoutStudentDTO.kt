package com.nuzhnov.workcontrol.core.api.model

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
 *                  id: 1,
 *                  name: "AVT-919",
 *                  course: 1
 *              },
 *
 *              {
 *                  id: 2,
 *                  name: "AVT-917",
 *                  course: 1
 *              }
 *          ],
 *
 *          theme: "Pointers",
 *          visit_type: "INTRAMURAL",
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
 *      note: "System Administrator | CTF | Java"
 * }
 */
@JsonClass(generateAdapter = true)
data class ParticipantWithoutStudentDTO(
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
