package com.nuzhnov.workcontrol.common.visitcontrol.mapper

import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitDebug


internal fun Visit.toVisitDebug() = VisitDebug(
    visitorID = visitorID,
    isActive = isActive,
    lastVisitTime = lastVisitDateTime,
    totalVisitDuration = totalVisitDuration
)
