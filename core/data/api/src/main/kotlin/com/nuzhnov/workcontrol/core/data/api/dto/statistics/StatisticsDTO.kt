package com.nuzhnov.workcontrol.core.data.api.dto.statistics

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      marks_percent: 56.3,
 *      avg_percent_visit_duration: 87.9,
 *      avg_theory_assessment: 4.4,
 *      avg_practice_assessment: null,
 *      avg_activity_assessment: 3.3,
 *      avg_prudence_assessment: 2.1,
 *      avg_creativity_assessment: 4.4,
 *      avg_preparation_assessment: 5.0,
 * }
 */
@JsonClass(generateAdapter = true)
data class StatisticsDTO(
    @Json(name = "marks_percent") val markPercent: Double?,
    @Json(name = "avg_percent_visit_duration") val avgPercentVisitDuration: Double?,
    @Json(name = "avg_theory_assessment") val avgTheoryAssessment: Double?,
    @Json(name = "avg_practice_assessment") val avgPracticeAssessment: Double?,
    @Json(name = "avg_activity_assessment") val avgActivityAssessment: Double?,
    @Json(name = "avg_prudence_assessment") val avgPrudenceAssessment: Double?,
    @Json(name = "avg_creativity_assessment") val avgCreativityAssessment: Double?,
    @Json(name = "avg_preparation_assessment") val avgPreparationAssessment: Double?,
)
