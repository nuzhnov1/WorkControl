package com.nuzhnov.workcontrol.core.database.entity.model

import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import androidx.room.Embedded
import androidx.room.Relation

data class ParticipantLessonModel(
    @Embedded
    val participantEntity: ParticipantEntity,

    @Relation(entity = LessonEntity::class, parentColumn = "lesson_id", entityColumn = "id")
    val lessonModel: LessonModel
)