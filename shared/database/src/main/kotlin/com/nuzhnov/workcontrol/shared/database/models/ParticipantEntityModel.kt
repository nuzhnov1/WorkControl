package com.nuzhnov.workcontrol.shared.database.models

import com.nuzhnov.workcontrol.shared.database.entity.StudentEntity
import com.nuzhnov.workcontrol.shared.database.entity.ParticipantEntity
import androidx.room.Embedded
import androidx.room.Relation

data class ParticipantEntityModel(
    @Embedded
    val participantEntity: ParticipantEntity,

    @Relation(parentColumn = "student_id", entityColumn = "id")
    val studentEntity: StudentEntity,

    @Relation(parentColumn = "lesson_id", entityColumn = "id")
    val lessonEntityModel: LessonEntityModel
)
