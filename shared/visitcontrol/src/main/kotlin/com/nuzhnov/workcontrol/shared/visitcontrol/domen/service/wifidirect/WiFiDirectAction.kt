package com.nuzhnov.workcontrol.shared.visitcontrol.domen.service.wifidirect

import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager.*

internal enum class WiFiDirectAction(val string: String?) {
    STATE_CHANGED(string = WIFI_P2P_STATE_CHANGED_ACTION),
    THIS_DEVICE_CHANGED(string = WIFI_P2P_THIS_DEVICE_CHANGED_ACTION),
    CONNECTION_CHANGED(string = WIFI_P2P_CONNECTION_CHANGED_ACTION),
    PEERS_CHANGED(string = WIFI_P2P_PEERS_CHANGED_ACTION)
}


internal fun createWiFiDirectAction(intent: Intent) = when (intent.action) {
    WIFI_P2P_STATE_CHANGED_ACTION -> WiFiDirectAction.STATE_CHANGED
    WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> WiFiDirectAction.THIS_DEVICE_CHANGED
    WIFI_P2P_CONNECTION_CHANGED_ACTION -> WiFiDirectAction.CONNECTION_CHANGED
    WIFI_P2P_PEERS_CHANGED_ACTION -> WiFiDirectAction.PEERS_CHANGED
    else -> null
}
