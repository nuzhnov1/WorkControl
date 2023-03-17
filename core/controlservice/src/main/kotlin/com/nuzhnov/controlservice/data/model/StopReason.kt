package com.nuzhnov.controlservice.data.model

import android.net.wifi.p2p.WifiP2pManager

enum class StopReason {
    SERVICE_DESTROYED,
    TECHNOLOGY_UNSUPPORTED,
    TECHNOLOGY_DISABLED,
    PERMISSION_DENIED,
    BUSY,
    INTERNAL_ERROR,
    DISCONNECTED
}


internal fun Int.toStopReason() = when (this) {
    WifiP2pManager.P2P_UNSUPPORTED -> StopReason.TECHNOLOGY_UNSUPPORTED
    WifiP2pManager.BUSY -> StopReason.BUSY
    else -> StopReason.INTERNAL_ERROR
}
