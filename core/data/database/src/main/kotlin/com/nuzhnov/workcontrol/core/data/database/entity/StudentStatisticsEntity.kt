package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "student_statistics",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["student_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class StudentStatisticsEntity(
    @[PrimaryKey(autoGenerate = false) ColumnInfo(name = "student_id", index = true)]
    val studentID: Long,

    @ColumnInfo(name = "marks_percent") val markPercent: Double?,
    @ColumnInfo(name = "avg_percent_visit_duration") val avgPercentVisitDuration: Double?,
    @ColumnInfo(name = "avg_theory_assessment") val avgTheoryAssessment: Double?,
    @ColumnInfo(name = "avg_practice_assessment") val avgPracticeAssessment: Double?,
    @ColumnInfo(name = "avg_activity_assessment") val avgActivityAssessment: Double?,
    @ColumnInfo(name = "avg_prudence_assessment") val avgPrudenceAssessment: Double?,
    @ColumnInfo(name = "avg_creativity_assessment") val avgCreativityAssessment: Double?,
    @ColumnInfo(name = "avg_preparation_assessment") val avgPreparationAssessment: Double?
)