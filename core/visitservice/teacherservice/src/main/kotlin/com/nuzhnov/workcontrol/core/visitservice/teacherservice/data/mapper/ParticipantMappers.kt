package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.mapper

import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.update.ParticipantActivityModel
import com.nuzhnov.workcontrol.core.data.mapper.toDateTimeTz
import com.nuzhnov.workcontrol.core.data.mapper.toTimeSpan
import com.nuzhnov.workcontrol.core.data.mapper.toLong
import com.nuzhnov.workcontrol.core.data.mapper.toDouble
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit


internal fun Visit.toParticipantActivityModel(lessonID: Long): ParticipantActivityModel =
    ParticipantActivityModel(
        studentID = visitorID,
        lessonID = lessonID,
        isActive = isActive,
        lastVisit = lastVisit?.toLong(),
        totalVisitDuration = totalVisitDuration.toDouble()
    )

internal fun ParticipantEntity.toVisit(): Visit = Visit(
    visitorID = studentID,
    isActive = isActive,
    lastVisit = lastVisit?.toDateTimeTz(),
    totalVisitDuration = totalVisitDuration.toTimeSpan()
)
