package com.nuzhnov.workcontrol.core.visitcontrol.mapper

import com.nuzhnov.workcontrol.core.visitcontrol.model.Visitor
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorDebug


internal fun Visitor.toVisitorDebug() = VisitorDebug(
    id = id,
    isActive = isActive,
    lastVisit = lastVisit,
    totalVisitDuration = totalVisitDuration
)
