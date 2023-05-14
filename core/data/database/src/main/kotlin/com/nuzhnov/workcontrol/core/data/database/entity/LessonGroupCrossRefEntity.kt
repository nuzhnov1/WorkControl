package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "lesson_student_group_cross_ref",
    primaryKeys = ["lesson_id", "group_id"],
    foreignKeys = [
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lesson_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onUpdate = ForeignKey.RESTRICT,
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class LessonGroupCrossRefEntity(
    @ColumnInfo(name = "lesson_id", index = true) val lessonID: Long,
    @ColumnInfo(name = "group_id", index = true) val groupID: Long
)
