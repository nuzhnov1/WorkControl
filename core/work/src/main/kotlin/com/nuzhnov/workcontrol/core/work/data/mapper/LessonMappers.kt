package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.lesson.NewLessonDTO
import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity


internal fun Pair<LessonEntity, Iterable<ParticipantEntity>>.toNewLessonDTO(): NewLessonDTO =
    let { (lessonEntity, associatedParticipants) ->
        NewLessonDTO(
            disciplineID = lessonEntity.disciplineID,
            teacherID = lessonEntity.teacherID,
            roomID = lessonEntity.roomID,
            theme = lessonEntity.theme,
            visitType = lessonEntity.visitType,
            startTime = lessonEntity.startTime,
            plannedDuration = lessonEntity.plannedDuration,
            actualDuration = lessonEntity.actualDuration,
            newParticipantDTOList = associatedParticipants.map(ParticipantEntity::toNewParticipantDTO)
        )
    }
