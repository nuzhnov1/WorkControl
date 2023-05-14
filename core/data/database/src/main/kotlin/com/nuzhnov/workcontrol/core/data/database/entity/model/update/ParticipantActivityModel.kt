package com.nuzhnov.workcontrol.core.data.database.entity.model.update

import androidx.room.ColumnInfo

data class ParticipantActivityModel(
    @ColumnInfo(name = "student_id") val studentID: Long,
    @ColumnInfo(name = "lesson_id") val lessonID: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "last_visit") val lastVisit: Long?,
    @ColumnInfo(name = "total_visit_duration") val totalVisitDuration: Double
)
