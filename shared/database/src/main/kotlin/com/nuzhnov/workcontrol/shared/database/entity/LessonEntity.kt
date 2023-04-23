package com.nuzhnov.workcontrol.shared.database.entity

import com.nuzhnov.workcontrol.shared.models.Lesson
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan

@Entity(
    tableName = "lesson",
    foreignKeys = [
        ForeignKey(
            entity = DisciplineEntity::class,
            parentColumns = ["id"],
            childColumns = ["discipline_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        ),

        ForeignKey(
            entity = TeacherEntity::class,
            parentColumns = ["id"],
            childColumns = ["teacher_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        ),

        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class LessonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "discipline_id", index = true) val disciplineID: Long,
    @ColumnInfo(name = "teacher_id", index = true) val teacherID: Long,
    @ColumnInfo(name = "room_id", index = true) val roomID: Long,
    val type: Lesson.Type,
    val theme: String,
    @ColumnInfo(name = "visit_type") val visitType: Lesson.VisitType,
    @ColumnInfo(name = "start_time") val startTime: DateTimeTz?,
    @ColumnInfo(name = "planned_duration") val plannedDuration: TimeSpan,
    @ColumnInfo(name = "actual_duration") val actualDuration: TimeSpan,
    val state: Lesson.State
)
