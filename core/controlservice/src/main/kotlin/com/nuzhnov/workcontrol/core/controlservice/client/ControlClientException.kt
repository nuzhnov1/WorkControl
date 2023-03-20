package com.nuzhnov.workcontrol.core.controlservice.client

internal sealed class ControlClientException(cause: Throwable) : Exception(cause) {
    override val cause: Throwable get() = super.cause!!

    class ConnectionFailedException(cause: Throwable) : ControlClientException(cause)
    class BreakConnectionException(cause: Throwable) : ControlClientException(cause)
    class BadConnectionException(cause: Throwable) : ControlClientException(cause)
    class ServerShutdownException(cause: Throwable) : ControlClientException(cause)
}
