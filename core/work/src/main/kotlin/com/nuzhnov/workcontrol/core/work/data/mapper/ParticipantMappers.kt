package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.lesson.NewParticipantDTO
import com.nuzhnov.workcontrol.core.api.dto.lesson.UpdatedParticipantDTO
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity


internal fun ParticipantEntity.toNewParticipantDTO(): NewParticipantDTO = NewParticipantDTO(
    studentID = studentID,
    totalVisitDuration = totalVisitDuration,
    isMarked = isMarked,
    theoryAssessment = theoryAssessment,
    practiceAssessment = practiceAssessment,
    activityAssessment = activityAssessment,
    prudenceAssessment = prudenceAssessment,
    creativityAssessment = creativityAssessment,
    preparationAssessment = preparationAssessment,
    note = note
)

internal fun ParticipantEntity.toUpdatedParticipantDTO(): UpdatedParticipantDTO =
    UpdatedParticipantDTO(
        lessonID = lessonID,
        studentID = studentID,
        totalVisitDuration = totalVisitDuration,
        isMarked = isMarked,
        theoryAssessment = theoryAssessment,
        practiceAssessment = practiceAssessment,
        activityAssessment = activityAssessment,
        prudenceAssessment = prudenceAssessment,
        creativityAssessment = creativityAssessment,
        preparationAssessment = preparationAssessment,
        note = note
    )
