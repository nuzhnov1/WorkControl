package com.nuzhnov.workcontrol.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "student_group",
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["department_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class GroupEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val course: Byte,
    @ColumnInfo(name = "department_id", index = true) val departmentID: Long
)
