package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "participant",
    primaryKeys = ["student_id", "lesson_id"],
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["student_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        ),

        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lesson_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ParticipantEntity(
    @ColumnInfo(name = "student_id", index = true) val studentID: Long,
    @ColumnInfo(name = "lesson_id", index = true) val lessonID: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "last_visit") val lastVisit: Long?,
    @ColumnInfo(name = "total_visit_duration") val totalVisitDuration: Double,
    @ColumnInfo(name = "is_marked") val isMarked: Boolean,
    @ColumnInfo(name = "theory_assessment") val theoryAssessment: Byte?,
    @ColumnInfo(name = "practice_assessment") val practiceAssessment: Byte?,
    @ColumnInfo(name = "activity_assessment") val activityAssessment: Byte?,
    @ColumnInfo(name = "prudence_assessment") val prudenceAssessment: Byte?,
    @ColumnInfo(name = "creativity_assessment") val creativityAssessment: Byte?,
    @ColumnInfo(name = "preparation_assessment") val preparationAssessment: Byte?,
    val note: String?,
    @ColumnInfo(name = "is_synchronized") val isSynchronized: Boolean
)
