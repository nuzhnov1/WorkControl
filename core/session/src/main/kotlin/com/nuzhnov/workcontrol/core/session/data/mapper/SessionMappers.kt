package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.preferences.model.Session


internal fun SessionDTO.toSession(): Session = Session(
    id = id,
    authorizationToken = authorizationToken,
    role = role
)
