package com.nuzhnov.workcontrol.core.data.database.entity.model

import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import androidx.room.Embedded
import androidx.room.Relation

data class ParticipantModel(
    @Embedded
    val participantEntity: ParticipantEntity,

    @Relation(entity = StudentEntity::class, parentColumn = "student_id", entityColumn = "id")
    val studentModel: StudentModel
)
