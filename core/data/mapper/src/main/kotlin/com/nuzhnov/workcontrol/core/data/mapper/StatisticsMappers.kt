package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Statistics
import com.nuzhnov.workcontrol.core.data.api.dto.statistics.StatisticsDTO
import com.nuzhnov.workcontrol.core.data.database.entity.UniversityStatisticsEntity
import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentStatisticsEntity
import com.nuzhnov.workcontrol.core.data.database.entity.GroupStatisticsEntity
import com.nuzhnov.workcontrol.core.data.database.entity.StudentStatisticsEntity


fun StatisticsDTO.toUniversityStatisticsEntity() = UniversityStatisticsEntity(
    id = 0L,
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)

fun StatisticsDTO.toDepartmentStatisticsEntity(departmentID: Long) = DepartmentStatisticsEntity(
    departmentID = departmentID,
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)

fun StatisticsDTO.toGroupStatisticsEntity(groupID: Long) = GroupStatisticsEntity(
    groupID = groupID,
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)

fun StatisticsDTO.toStudentStatisticsEntity(studentID: Long) = StudentStatisticsEntity(
    studentID = studentID,
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)

fun UniversityStatisticsEntity.toStatistics() = Statistics(
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)

fun DepartmentStatisticsEntity.toStatistics() = Statistics(
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)

fun GroupStatisticsEntity.toStatistics() = Statistics(
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)

fun StudentStatisticsEntity.toStatistics() = Statistics(
    markPercent = markPercent,
    avgPercentVisitDuration = avgPercentVisitDuration,
    avgTheoryAssessment = avgTheoryAssessment,
    avgPracticeAssessment = avgPracticeAssessment,
    avgActivityAssessment = avgActivityAssessment,
    avgPrudenceAssessment = avgPrudenceAssessment,
    avgCreativityAssessment = avgCreativityAssessment,
    avgPreparationAssessment = avgPreparationAssessment
)
