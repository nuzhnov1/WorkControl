package com.nuzhnov.workcontrol.core.data.api.dto.lesson

import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentModelDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      student: {
 *          student: {
 *              id: 1,
 *              name: "Bakharov Malik",
 *              email: "mufti@mail.ru"
 *          },
 *
 *          group: {
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
 *          }
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
data class ParticipantModelDTO(
    @Json(name = "student") val studentModelDTO: StudentModelDTO,
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
