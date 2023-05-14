package com.nuzhnov.workcontrol.core.data.database.entity.model.update

import androidx.room.ColumnInfo

data class ParticipantUpdatableModel(
    @ColumnInfo(name = "student_id") val studentID: Long,
    @ColumnInfo(name = "lesson_id") val lessonID: Long,
    @ColumnInfo(name = "is_marked") val isMarked: Boolean,
    @ColumnInfo(name = "theory_assessment") val theoryAssessment: Byte?,
    @ColumnInfo(name = "practice_assessment") val practiceAssessment: Byte?,
    @ColumnInfo(name = "activity_assessment") val activityAssessment: Byte?,
    @ColumnInfo(name = "prudence_assessment") val prudenceAssessment: Byte?,
    @ColumnInfo(name = "creativity_assessment") val creativityAssessment: Byte?,
    @ColumnInfo(name = "preparation_assessment") val preparationAssessment: Byte?,
    val note: String?
)
