package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "lesson_student_group_cross_ref",
    primaryKeys = ["lesson_id", "student_group_id"],
    foreignKeys = [
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["lesson_id"],
            childColumns = ["lesson_id"],
            onUpdate = ForeignKey.RESTRICT,
            onDelete = ForeignKey.RESTRICT
        ),

        ForeignKey(
            entity = StudentGroupEntity::class,
            parentColumns = ["student_group_id"],
            childColumns = ["student_group_id"],
            onUpdate = ForeignKey.RESTRICT,
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class LessonGroupCrossRefEntity(
    @ColumnInfo(name = "lesson_id", index = true) val lessonID: Long,
    @ColumnInfo(name = "student_group_id", index = true) val groupID: Long
)
