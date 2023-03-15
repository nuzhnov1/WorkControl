@file:Suppress("DEPRECATION")

package com.nuzhnov.controlservice.wifidirect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import com.nuzhnov.controlservice.WiFiDirectControlService
import com.nuzhnov.controlservice.data.StopReason

internal class WiFiDirectBroadcastReceiver(
    private val service: WiFiDirectControlService
) : BroadcastReceiver() {

    private val wifiDirectManager get() = service.wifiDirectManager
    private val wifiDirectChannel get() = service.wifiDirectChannel


    override fun onReceive(context: Context, intent: Intent) {
        when (createWiFiDirectAction(intent)) {
            null -> Unit

            WiFiDirectAction.STATE_CHANGED -> {
                // When Wi-Fi Direct on this device is enabled or disabled

                val state = intent.getIntExtra(
                    /* name = */ WifiP2pManager.EXTRA_WIFI_STATE,
                    /* defaultValue = */ WifiP2pManager.WIFI_P2P_STATE_DISABLED
                )

                if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
                    // TODO: notify user about disabled p2p status
                    service.onStopService(StopReason.TECHNOLOGY_DISABLED)
                }
            }

            WiFiDirectAction.THIS_DEVICE_CHANGED -> {
                // When this device's configuration details have changed

                val thisWifiP2pDevice: WifiP2pDevice = intent
                    .getParcelable(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)!!

                service.thisWifiDirectDevice = thisWifiP2pDevice
            }

            WiFiDirectAction.CONNECTION_CHANGED -> {
                // When state of Wi-Fi Direct connectivity has changed on this device

                val networkInfo: NetworkInfo = intent
                    .getParcelable(WifiP2pManager.EXTRA_NETWORK_INFO)!!

                // TODO: check network info
                if (!networkInfo.isConnected) {
                    // TODO: notify user about disconnected
                    // TODO: Maybe just suspend the service instead of stopping it completely
                    service.onStopService(StopReason.DISCONNECTED)
                }
            }

            WiFiDirectAction.PEERS_CHANGED -> {
                // When available peers list has changed

                val manager = wifiDirectManager ?: return
                val channel = wifiDirectChannel ?: return

                manager.requestGroupInfo(channel, service::onPeerGroupUpdated)
            }
        }
    }
}
