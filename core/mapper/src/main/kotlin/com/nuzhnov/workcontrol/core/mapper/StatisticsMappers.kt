package com.nuzhnov.workcontrol.core.mapper

import com.nuzhnov.workcontrol.core.model.Statistics
import com.nuzhnov.workcontrol.core.api.dto.statistics.StatisticsDTO


fun StatisticsDTO.toStatistics(): Statistics = Statistics(
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)
