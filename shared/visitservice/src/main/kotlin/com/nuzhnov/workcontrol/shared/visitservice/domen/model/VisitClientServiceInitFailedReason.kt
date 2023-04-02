package com.nuzhnov.workcontrol.shared.visitservice.domen.model

enum class VisitClientServiceInitFailedReason {
    SERVICE_INTENT_MISSED,
    SERVICE_NAME_EXTRA_MISSED,
    CLIENT_ID_EXTRA_MISSED,
    TECHNOLOGY_UNSUPPORTED_ERROR,
    TECHNOLOGY_DISABLED_ERROR,
    PERMISSION_DENIED_ERROR
}
