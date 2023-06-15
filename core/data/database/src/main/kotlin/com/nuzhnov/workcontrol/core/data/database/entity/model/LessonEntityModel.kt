package com.nuzhnov.workcontrol.core.data.database.entity.model

import com.nuzhnov.workcontrol.core.data.database.entity.*
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Junction

data class LessonEntityModel(
    @Embedded
    val lessonEntity: LessonEntity,

    @Relation(
        entity = GroupEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = LessonGroupCrossRefEntity::class,
            parentColumn = "lesson_id",
            entityColumn = "group_id"
        )
    )
    val groupEntityModelList: List<GroupEntityModel>,

    @Relation(parentColumn = "teacher_id", entityColumn = "id")
    val teacherEntity: TeacherEntity,

    @Relation(parentColumn = "discipline_id", entityColumn = "id")
    val disciplineEntity: DisciplineEntity,

    @Relation(entity = RoomEntity::class, parentColumn = "room_id", entityColumn = "id")
    val roomEntityModel: RoomEntityModel
)
