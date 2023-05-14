package com.nuzhnov.workcontrol.core.data.database.entity.model

import com.nuzhnov.workcontrol.core.data.database.entity.TeacherEntity
import com.nuzhnov.workcontrol.core.data.database.entity.TeacherDisciplineCrossRefEntity
import com.nuzhnov.workcontrol.core.data.database.entity.DisciplineEntity
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Junction

data class TeacherModel(
    @Embedded
    val teacherEntity: TeacherEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TeacherDisciplineCrossRefEntity::class,
            parentColumn = "teacher_id",
            entityColumn = "discipline_id"
        )
    )
    val disciplineEntityList: List<DisciplineEntity>
)
