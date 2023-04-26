package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "student_group",
    foreignKeys = [
        ForeignKey(
            entity = FacultyEntity::class,
            parentColumns = ["faculty_id"],
            childColumns = ["faculty_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class StudentGroupEntity(
    @[PrimaryKey(autoGenerate = false) ColumnInfo(name = "student_group_id")]
    val studentGroupID: Long,
    val name: String,
    val course: Byte,
    @ColumnInfo(name = "faculty_id", index = true)
    val facultyID: Long
)
