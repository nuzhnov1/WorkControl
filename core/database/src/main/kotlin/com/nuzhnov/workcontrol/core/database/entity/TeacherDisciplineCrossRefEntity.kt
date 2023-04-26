package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "teacher_discipline_cross_ref",
    primaryKeys = ["teacher_id", "discipline_id"],
    foreignKeys = [
        ForeignKey(
            entity = TeacherEntity::class,
            parentColumns = ["teacher_id"],
            childColumns = ["teacher_id"],
            onUpdate = ForeignKey.RESTRICT,
            onDelete = ForeignKey.RESTRICT
        ),

        ForeignKey(
            entity = DisciplineEntity::class,
            parentColumns = ["discipline_id"],
            childColumns = ["discipline_id"],
            onUpdate = ForeignKey.RESTRICT,
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class TeacherDisciplineCrossRefEntity(
    @ColumnInfo(name = "teacher_id", index = true) val teacherID: Long,
    @ColumnInfo(name = "discipline_id", index = true) val disciplineID: Long
)
