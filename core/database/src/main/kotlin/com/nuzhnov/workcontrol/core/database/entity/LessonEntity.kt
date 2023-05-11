package com.nuzhnov.workcontrol.core.database.entity

import com.nuzhnov.workcontrol.core.model.Lesson
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

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
    val theme: String,
    val type: Lesson.Type,
    @ColumnInfo(name = "start_time") val startTime: Long?,
    @ColumnInfo(name = "planned_duration") val plannedDuration: Double,
    @ColumnInfo(name = "actual_duration") val actualDuration: Double?,
    val state: Lesson.State,
    @ColumnInfo(name = "is_synchronized") val isSynchronised: Boolean
)
