package com.nuzhnov.controlservice.data.api.server

enum class ControlServerError {
    INIT_ERROR,
    MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED_ERROR,
    IO_ERROR,
    SECURITY_ERROR,
    UNKNOWN_ERROR
}
