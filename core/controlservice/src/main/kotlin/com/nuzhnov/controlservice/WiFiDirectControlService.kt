package com.nuzhnov.controlservice

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.nuzhnov.controlservice.wifidirect.WiFiDirectBroadcastReceiver
import com.nuzhnov.controlservice.wifidirect.WiFiDirectAction

/* TODO: check Wi-Fi direct compatibility and dangerous permissions in separate function,
   that starts service. Also this function responsible to enable Wi-Fi Direct (moving user to
   Settings app).

   Send messages of service using intent and broadcast
*/

class WiFiDirectControlService : ControlBaseService(),
    WifiP2pManager.ConnectionInfoListener,
    WifiP2pManager.GroupInfoListener {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkListener: WifiDirectNetworkListener

    internal lateinit var wifiP2pManager: WifiP2pManager
    internal lateinit var channel: WifiP2pManager.Channel
    private lateinit var receiver: WiFiDirectBroadcastReceiver

    private var isInitialized = false
    private var isStarted = false

    private val receiverIntentFilter = IntentFilter().apply {
        WiFiDirectAction.values().forEach { action -> addAction(action.string) }
    }


    override fun onCreate() {
        wifiP2pManager = getSystemService(Context.WIFI_P2P_SERVICE) as? WifiP2pManager ?: return
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return
        channel = wifiP2pManager.initialize(
            /* srcContext = */ this,
            /* srcLooper = */ mainLooper,
            /* listener = */ null
        )
        receiver = WiFiDirectBroadcastReceiver(service = this)

        isInitialized = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isInitialized) {
            stopSelf()
            return START_NOT_STICKY
        }

        if (!isStarted) {
            startService()
        }

        return START_STICKY
    }

    /**
     * This service doesn't support binding, so this method always returns null
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        if (isStarted) {
            stopService()
        }

        isInitialized = false
    }

    override fun onConnectionInfoAvailable(wifiP2pInfo: WifiP2pInfo) {
        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
            val groupOwnerAddress = wifiP2pInfo.groupOwnerAddress
            // TODO: Start listening for clients and requesting group info
            // TODO: Requesting group info
        }
    }

    override fun onGroupInfoAvailable(wifiP2pGroup: WifiP2pGroup) {
        TODO("Not yet implemented")
    }

    private fun startService() {
        isStarted = true

        if (Build.VERSION.SDK_INT >= 21) {
            registerNetworkListener()
        }

        registerReceiver(receiver, receiverIntentFilter)
        wifiP2pManager.createGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // P2P group successfully created
            }

            override fun onFailure(reason: Int) {
                TODO("Stop service and report about disconnected")
            }
        })
    }

    private fun stopService() {
        isStarted = false

        if (Build.VERSION.SDK_INT >= 21) {
            unregisterNetworkListener()
        }

        unregisterReceiver(receiver)
        wifiP2pManager.removeGroup(channel, /* listener = */ null)
        stopSelf()
    }

    @RequiresApi(api = 21)
    private fun registerNetworkListener() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        networkListener = WifiDirectNetworkListener()
        connectivityManager.registerNetworkCallback(networkRequest, networkListener)
    }

    @RequiresApi(api = 21)
    private fun unregisterNetworkListener() {
        connectivityManager.unregisterNetworkCallback(networkListener)
    }


    @RequiresApi(api = 21)
    private inner class WifiDirectNetworkListener : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            wifiP2pManager.requestConnectionInfo(channel, ::onConnectionInfoAvailable)
        }

        override fun onLost(network: Network) {
            TODO("Stop service and report error")
        }
    }
}
