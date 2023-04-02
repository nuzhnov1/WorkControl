package com.nuzhnov.workcontrol.shared.visitservice.domen.model

import java.net.InetAddress
import android.content.Intent

sealed interface VisitControlServiceState {
    object NotInitialized : VisitControlServiceState

    data class Initializing(
        internal val intent: Intent?,
        internal val startId: Int
    ) : VisitControlServiceState

    data class InitFailed(
        val reason: VisitControlServiceInitFailedReason
    ) : VisitControlServiceState

    object Initialized : VisitControlServiceState

    object NotRunning : VisitControlServiceState

    data class Running(
        val serverAddress: InetAddress,
        val serverPort: Int
    ) : VisitControlServiceState

    data class StoppedAcceptConnections(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val error: VisitControlServiceError
    ) : VisitControlServiceState

    data class Stopped(
        val serverAddress: InetAddress,
        val serverPort: Int
    ) : VisitControlServiceState

    data class StoppedByError(
        val error: VisitControlServiceError
    ) : VisitControlServiceState
}
