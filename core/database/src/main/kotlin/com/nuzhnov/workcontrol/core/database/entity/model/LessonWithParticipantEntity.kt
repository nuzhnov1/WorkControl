package com.nuzhnov.workcontrol.core.database.entity.model

import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import androidx.room.Embedded
import androidx.room.Relation

data class LessonWithParticipantEntity(
    @Embedded
    val lessonEntity: LessonEntity,

    @Relation(parentColumn = "id", entityColumn = "lesson_id")
    val participantEntityList: List<ParticipantEntity>
)