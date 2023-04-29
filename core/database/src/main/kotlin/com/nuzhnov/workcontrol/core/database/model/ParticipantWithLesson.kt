package com.nuzhnov.workcontrol.core.database.model

import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import androidx.room.Embedded
import androidx.room.Relation

data class ParticipantWithLesson(
    @Embedded
    val participantEntity: ParticipantEntity,

    @Relation(parentColumn = "student_id", entityColumn = "id")
    val studentEntity: StudentEntity,

    @Relation(entity = LessonEntity::class, parentColumn = "lesson_id", entityColumn = "id")
    val lessonEntityModel: LessonEntityModel
)
