package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.data.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.data.preferences.model.Session


internal fun SessionDTO.toSession() = Session(
    id = id,
    authorizationToken = authorizationToken,
    role = role
)
