@file:Suppress("DEPRECATION")

package com.nuzhnov.controlservice.wifidirect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import com.nuzhnov.controlservice.WiFiDirectControlService

internal class WiFiDirectBroadcastReceiver(
    private val service: WiFiDirectControlService
) : BroadcastReceiver() {

    private val wifiP2pManager get() = service.wifiP2pManager
    private val channel get() = service.channel


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
                    TODO("Stop service and report about disabled p2p")
                }
            }

            WiFiDirectAction.THIS_DEVICE_CHANGED -> {
                // When this device's configuration details have changed

                val thisWifiP2pDevice: WifiP2pDevice = intent.getParcelable(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)!!
                TODO("Send this p2p device information by broadcast using intent")
            }

            WiFiDirectAction.CONNECTION_CHANGED -> {
                // When state of Wi-Fi Direct connectivity has changed on this device

                // On device's running on Android 5 or higher we using ConnectivityManager.NetworkCallback
                if (Build.VERSION.SDK_INT < 21) {
                    val networkInfo: NetworkInfo = intent.getParcelable(WifiP2pManager.EXTRA_NETWORK_INFO)!!

                    if (networkInfo.isConnected) {
                        wifiP2pManager.requestConnectionInfo(channel, service::onConnectionInfoAvailable)
                    } else {
                        TODO("Stop service and report about disconnected")
                    }
                }
            }

            WiFiDirectAction.PEERS_CHANGED -> {
                // When available peers list has changed

                wifiP2pManager.requestGroupInfo(channel, service::onGroupInfoAvailable)
            }
        }
    }
}
