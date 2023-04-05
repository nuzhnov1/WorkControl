package com.nuzhnov.workcontrol.core.visitcontrol.mapper

import com.nuzhnov.workcontrol.core.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitDebug


internal fun Visit.toVisitDebug() = VisitDebug(
    visitorID = visitorID,
    isActive = isActive,
    lastVisitTime = lastVisitTime,
    totalVisitDuration = totalVisitDuration
)
