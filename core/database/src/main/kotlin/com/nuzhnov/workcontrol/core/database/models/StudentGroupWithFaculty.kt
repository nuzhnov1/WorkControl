package com.nuzhnov.workcontrol.core.database.models

import com.nuzhnov.workcontrol.core.database.entity.StudentGroupEntity
import com.nuzhnov.workcontrol.core.database.entity.FacultyEntity
import androidx.room.Embedded
import androidx.room.Relation

data class StudentGroupWithFaculty(
    @Embedded
    val studentGroupEntity: StudentGroupEntity,

    @Relation(parentColumn = "faculty_id", entityColumn = "id")
    val facultyEntity: FacultyEntity
)
