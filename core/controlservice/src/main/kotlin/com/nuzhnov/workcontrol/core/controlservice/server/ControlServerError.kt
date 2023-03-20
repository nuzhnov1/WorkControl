package com.nuzhnov.workcontrol.core.controlservice.server

enum class ControlServerError {
    INIT_ERROR,
    MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED,
    IO_ERROR,
    SECURITY_ERROR,
    UNKNOWN_ERROR
}
