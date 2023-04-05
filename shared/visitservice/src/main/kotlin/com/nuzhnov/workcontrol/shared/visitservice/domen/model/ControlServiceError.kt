package com.nuzhnov.workcontrol.shared.visitservice.domen.model

enum class ControlServiceError {
    REGISTER_SERVICE_FAILED,
    INIT_ERROR,
    MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
    IO_ERROR,
    SECURITY_ERROR,
    UNKNOWN_ERROR
}
