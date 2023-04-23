package com.nuzhnov.workcontrol.shared.database.models

import androidx.room.ColumnInfo
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan

data class ParticipantActivity(
    @ColumnInfo(name = "student_id") val studentID: Long,
    @ColumnInfo(name = "lesson_id") val lessonID: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "last_visit") val lastVisit: DateTimeTz?,
    @ColumnInfo(name = "total_visit_duration") val totalVisitDuration: TimeSpan
)
