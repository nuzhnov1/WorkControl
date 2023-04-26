package com.nuzhnov.workcontrol.core.database.models

import androidx.room.ColumnInfo
import com.soywiz.klock.DateTimeTz

data class ParticipantActivity(
    @ColumnInfo(name = "student_id") val studentID: Long,
    @ColumnInfo(name = "lesson_id") val lessonID: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "last_visit") val lastVisit: DateTimeTz?,
    @ColumnInfo(name = "total_visit_duration") val totalVisitDuration: Double
)
