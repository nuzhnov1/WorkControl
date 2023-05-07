package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.authorization.SessionDTO
import com.nuzhnov.workcontrol.core.preferences.model.Session


internal fun SessionDTO.toSession(login: String): Session = Session(
    id = id,
    login = login,
    authorizationToken = authorizationToken,
    role = role
)
