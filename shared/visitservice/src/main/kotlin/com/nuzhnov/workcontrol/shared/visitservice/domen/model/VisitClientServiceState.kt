package com.nuzhnov.workcontrol.shared.visitservice.domen.model

import java.net.InetAddress
import android.content.Intent

sealed interface VisitClientServiceState {
    object NotInitialized : VisitClientServiceState

    data class Initializing(
        internal val intent: Intent?,
        internal val startId: Int
    ) : VisitClientServiceState

    data class InitFailed(
        val reason: VisitClientServiceInitFailedReason
    ) : VisitClientServiceState

    object Initialized : VisitClientServiceState

    object NotRunning : VisitClientServiceState

    data class Connecting(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val clientID: Long
    ) : VisitClientServiceState

    data class Running(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val clientID: Long
    ) : VisitClientServiceState

    data class Stopped(
        val serverAddress: InetAddress,
        val serverPort: Int,
        val clientID: Long
    ) : VisitClientServiceState

    data class StoppedByError(
        val error: VisitClientServiceError
    ) : VisitClientServiceState
}
