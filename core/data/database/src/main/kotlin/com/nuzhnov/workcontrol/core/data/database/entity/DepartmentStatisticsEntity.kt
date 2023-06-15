package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "department_statistics",
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["department_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class DepartmentStatisticsEntity(
    @[PrimaryKey(autoGenerate = false) ColumnInfo(name = "department_id", index = true)]
    val departmentID: Long,

    @ColumnInfo(name = "marks_percent") val markPercent: Double?,
    @ColumnInfo(name = "avg_percent_visit_duration") val avgPercentVisitDuration: Double?,
    @ColumnInfo(name = "avg_theory_assessment") val avgTheoryAssessment: Double?,
    @ColumnInfo(name = "avg_practice_assessment") val avgPracticeAssessment: Double?,
    @ColumnInfo(name = "avg_activity_assessment") val avgActivityAssessment: Double?,
    @ColumnInfo(name = "avg_prudence_assessment") val avgPrudenceAssessment: Double?,
    @ColumnInfo(name = "avg_creativity_assessment") val avgCreativityAssessment: Double?,
    @ColumnInfo(name = "avg_preparation_assessment") val avgPreparationAssessment: Double?
)
