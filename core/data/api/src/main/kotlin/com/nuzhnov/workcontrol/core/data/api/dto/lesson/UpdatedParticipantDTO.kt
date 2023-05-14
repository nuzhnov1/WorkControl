package com.nuzhnov.workcontrol.core.data.api.dto.lesson

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      lesson_id: 1,
 *      student_id: 1,
 *      total_visit_duration: 10293809128.20198340921384,
 *      is_marked: true,
 *      theory_assessment: 5,
 *      practice_assessment: 2,
 *      activity_assessment: null,
 *      prudence_assessment: null,
 *      creativity_assessment: null,
 *      preparation_assessment: 2,
 *      note: Cool guy
 * }
 */
@JsonClass(generateAdapter = true)
data class UpdatedParticipantDTO(
    @Json(name = "lesson_id") val lessonID: Long,
    @Json(name = "student_id") val studentID: Long,
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
