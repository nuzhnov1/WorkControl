package com.nuzhnov.workcontrol.common.visitcontrol.visitor

internal sealed class VisitorException(cause: Throwable) : Exception(cause) {
    override val cause: Throwable get() = super.cause!!

    class ConnectionFailedException(cause: Throwable) : VisitorException(cause)
    class BreakConnectionException(cause: Throwable) : VisitorException(cause)
    class BadConnectionException(cause: Throwable) : VisitorException(cause)
    class DisconnectedException(cause: Throwable) : VisitorException(cause)
}
