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
        parentColumn = "lesson_id",
        entityColumn = "student_group_id",
        associateBy = Junction(
            value = LessonGroupCrossRefEntity::class,
            parentColumn = "lesson_id",
            entityColumn = "student_group_id"
        )
    )
    val studentGroupWithFaculty: List<StudentGroupWithFaculty>,

    @Relation(parentColumn = "teacher_id", entityColumn = "teacher_id")
    val teacherEntity: TeacherEntity,

    @Relation(parentColumn = "discipline_id", entityColumn = "discipline_id")
    val disciplineEntity: DisciplineEntity,

    @Relation(parentColumn = "room_id", entityColumn = "room_id")
    val roomWithBuilding: RoomWithBuilding
)
