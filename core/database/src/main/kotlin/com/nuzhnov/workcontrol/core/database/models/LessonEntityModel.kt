package com.nuzhnov.workcontrol.core.database.models

import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.LessonGroupCrossRefEntity
import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity
import com.nuzhnov.workcontrol.core.database.entity.TeacherEntity
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Junction

data class LessonEntityModel(
    @Embedded
    val lessonEntity: LessonEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = LessonGroupCrossRefEntity::class,
            parentColumn = "lesson_id",
            entityColumn = "group_id"
        )
    )
    val groupWithFaculty: List<GroupWithFaculty>,

    @Relation(parentColumn = "teacher_id", entityColumn = "id")
    val teacherEntity: TeacherEntity,

    @Relation(parentColumn = "discipline_id", entityColumn = "id")
    val disciplineEntity: DisciplineEntity,

    @Relation(parentColumn = "room_id", entityColumn = "id")
    val roomWithBuilding: RoomWithBuilding
)
