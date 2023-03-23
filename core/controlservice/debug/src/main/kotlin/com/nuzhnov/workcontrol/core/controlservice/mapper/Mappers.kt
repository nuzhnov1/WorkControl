package com.nuzhnov.workcontrol.core.controlservice.mapper

import com.nuzhnov.workcontrol.core.controlservice.model.Client
import com.nuzhnov.workcontrol.core.controlservice.model.ClientApiModel


internal fun ClientApiModel.toClient() = Client(
    id = id,
    isActive = isActive,
    lastVisit = lastVisit,
    totalVisitDuration = totalVisitDuration
)
