package com.nuzhnov.workcontrol.core.data.database.entity.model

import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.data.database.entity.GroupEntity
import androidx.room.Embedded
import androidx.room.Relation

data class StudentModel(
    @Embedded
    val studentEntity: StudentEntity,

    @Relation(
        entity = GroupEntity::class,
        parentColumn = "group_id",
        entityColumn = "id"
    )
    val groupModel: GroupModel
)
