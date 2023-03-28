package com.nuzhnov.workcontrol.core.visitcontrol.client

internal sealed class ClientException(cause: Throwable) : Exception(cause) {
    override val cause: Throwable get() = super.cause!!

    class ConnectionFailedException(cause: Throwable) : ClientException(cause)
    class BreakConnectionException(cause: Throwable) : ClientException(cause)
    class BadConnectionException(cause: Throwable) : ClientException(cause)
    class ServerShutdownException(cause: Throwable) : ClientException(cause)
}
