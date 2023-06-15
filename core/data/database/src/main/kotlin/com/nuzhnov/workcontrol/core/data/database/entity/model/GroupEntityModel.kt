package com.nuzhnov.workcontrol.core.data.database.entity.model

import com.nuzhnov.workcontrol.core.data.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentEntity
import androidx.room.Embedded
import androidx.room.Relation

data class GroupEntityModel(
    @Embedded
    val groupEntity: GroupEntity,

    @Relation(parentColumn = "department_id", entityColumn = "id")
    val departmentEntity: DepartmentEntity
)
