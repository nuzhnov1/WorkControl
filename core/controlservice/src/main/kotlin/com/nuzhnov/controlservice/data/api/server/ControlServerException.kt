package com.nuzhnov.controlservice.data.api.server

internal sealed class ControlServerException(cause: Throwable) : Exception(cause) {
    override val cause: Throwable get() = super.cause!!


    class InitException(
        cause: Throwable
    ) : ControlServerException(cause)

    class MaxAcceptConnectionAttemptsReachedException(
        cause: Throwable
    ) : ControlServerException(cause)
}
