package com.nuzhnov.workcontrol.core.visitcontrol.server

internal sealed class ServerException(cause: Throwable) : Exception(cause) {
    override val cause: Throwable get() = super.cause!!


    class InitException(
        cause: Throwable
    ) : ServerException(cause)

    class MaxAcceptConnectionAttemptsReachedException(
        cause: Throwable
    ) : ServerException(cause)
}
