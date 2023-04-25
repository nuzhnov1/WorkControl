package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "student",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["student_group_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class StudentEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val email: String,
    @ColumnInfo(name = "student_group_id", index = true) val groupID: Long
)
