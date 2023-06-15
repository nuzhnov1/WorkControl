package com.nuzhnov.workcontrol.core.data.database.entity.model

import com.nuzhnov.workcontrol.core.data.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import androidx.room.Embedded
import androidx.room.Relation

data class LessonWithParticipantsModel(
    @Embedded
    val lessonEntity: LessonEntity,

    @Relation(parentColumn = "id", entityColumn = "lesson_id")
    val participantEntityList: List<ParticipantEntity>
)
