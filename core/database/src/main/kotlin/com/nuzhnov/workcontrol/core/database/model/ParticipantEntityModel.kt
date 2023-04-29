package com.nuzhnov.workcontrol.core.database.model

import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import androidx.room.Embedded
import androidx.room.Relation

data class ParticipantEntityModel(
    @Embedded
    val participantEntity: ParticipantEntity,

    @Relation(parentColumn = "student_id", entityColumn = "id")
    val studentEntity: StudentEntity
)
