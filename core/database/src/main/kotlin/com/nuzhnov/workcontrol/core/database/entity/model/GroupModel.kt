package com.nuzhnov.workcontrol.core.database.entity.model

import com.nuzhnov.workcontrol.core.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.database.entity.FacultyEntity
import androidx.room.Embedded
import androidx.room.Relation

data class GroupModel(
    @Embedded
    val groupEntity: GroupEntity,

    @Relation(parentColumn = "faculty_id", entityColumn = "id")
    val facultyEntity: FacultyEntity
)
