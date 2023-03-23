package com.nuzhnov.workcontrol.core.controlservice.mapper

import com.nuzhnov.workcontrol.core.controlservice.model.Client
import com.nuzhnov.workcontrol.core.controlservice.model.ClientApiModel


fun ClientApiModel.toClient() = Client(
    id = id,
    isActive = isActive,
    lastVisit = lastVisit,
    totalVisitDuration = totalVisitDuration
)
