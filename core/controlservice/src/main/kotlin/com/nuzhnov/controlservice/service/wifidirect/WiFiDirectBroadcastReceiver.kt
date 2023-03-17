@file:Suppress("DEPRECATION")

package com.nuzhnov.controlservice.service.wifidirect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.*
import com.nuzhnov.controlservice.data.model.StopReason

internal class WiFiDirectBroadcastReceiver(
    private val wifiDirectService: WiFiDirectControlService,
    private val wifiDirectManager: WifiP2pManager,
    private val wifiDirectChannel: Channel
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (createWiFiDirectAction(intent)) {
            null -> Unit

            WiFiDirectAction.STATE_CHANGED -> {
                // When Wi-Fi Direct on this device is enabled or disabled

                val state = intent.getIntExtra(
                    /* name = */ EXTRA_WIFI_STATE,
                    /* defaultValue = */ WIFI_P2P_STATE_DISABLED
                )

                if (state == WIFI_P2P_STATE_DISABLED) {
                    wifiDirectService.onStopService(StopReason.TECHNOLOGY_DISABLED)
                }
            }

            WiFiDirectAction.THIS_DEVICE_CHANGED -> {
                // When information about this device has changed

                val wifiP2pDevice = intent.getParcelable<WifiP2pDevice>(EXTRA_WIFI_P2P_DEVICE)!!
                wifiDirectService.onThisDeviceUpdated(wifiP2pDevice)
            }

            WiFiDirectAction.CONNECTION_CHANGED -> {
                // When state of Wi-Fi Direct connectivity has changed on this device

                val networkInfo: NetworkInfo = intent.getParcelable(EXTRA_NETWORK_INFO)!!

                if (networkInfo.isConnected) {
                    wifiDirectManager.requestConnectionInfo(wifiDirectChannel, wifiDirectService)
                } else {
                    wifiDirectService.onStopService(StopReason.DISCONNECTED)
                }
            }

            WiFiDirectAction.PEERS_CHANGED -> {
                // When available peers list has changed

                wifiDirectManager.requestGroupInfo(wifiDirectChannel, wifiDirectService)
            }
        }
    }
}
