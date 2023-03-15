package com.nuzhnov.controlservice

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.NEARBY_WIFI_DEVICES
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.pm.PackageManager.FEATURE_WIFI_DIRECT
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.nuzhnov.controlservice.data.StopReason
import com.nuzhnov.controlservice.data.WiFiDirectControlServiceRepository
import com.nuzhnov.controlservice.data.toStopReason
import com.nuzhnov.controlservice.wifidirect.WiFiDirectBroadcastReceiver
import com.nuzhnov.controlservice.wifidirect.WiFiDirectAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: think about handling connection breaks

@AndroidEntryPoint
class WiFiDirectControlService @Inject constructor(
    override val repository: WiFiDirectControlServiceRepository
) : ControlService() {

    internal var wifiDirectManager: WifiP2pManager? = null
    internal var wifiDirectChannel: WifiP2pManager.Channel? = null
    private var wifiDirectReceiver: WiFiDirectBroadcastReceiver? = null

    internal var thisWifiDirectDevice
        get() = repository.wifiDirectDevice.value
        set(value) = repository.setThisDevice(value)


    override fun onInitService() {
        super.onInitService()

        if (!isWiFiDirectSupported()) {
            onInitServiceFailed(StopReason.TECHNOLOGY_UNSUPPORTED)
            return
        }

        if (Build.VERSION.SDK_INT >= 21 && !isWiFiDirectEnabled()) {
            onInitServiceFailed(StopReason.TECHNOLOGY_DISABLED)
            return
        }

        if (Build.VERSION.SDK_INT >= 23 && !isPermissionsGranted()) {
            onInitServiceFailed(StopReason.PERMISSION_DENIED)
            return
        }

        val manager = getSystemService(WIFI_P2P_SERVICE) as? WifiP2pManager
        if (manager == null) {
            onInitServiceFailed(StopReason.TECHNOLOGY_DISABLED)
            return
        } else {
            wifiDirectManager = manager
        }

        val channel = manager.initialize(this, mainLooper, null)
        if (channel == null) {
            onInitServiceFailed(StopReason.TECHNOLOGY_DISABLED)
            return
        } else {
            wifiDirectChannel = channel
        }

        wifiDirectReceiver = WiFiDirectBroadcastReceiver(service = this)
    }

    override fun onStartService(startId: Int) {
        super.onStartService(startId)

        val receiverIntentFilter = IntentFilter().apply {
            WiFiDirectAction.values().forEach { action -> addAction(action.string) }
        }

        registerReceiver(wifiDirectReceiver!!, receiverIntentFilter)
        wifiDirectManager!!.createGroup(wifiDirectChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // TODO: notify user about successfully created p2p group
            }

            override fun onFailure(reason: Int) {
                // TODO: notify user about failed to create p2p group
                // TODO: maybe recreate group N times with interval M seconds
                onStopService(reason.toStopReason())
            }
        })
    }

    override fun onStopService(stopReason: StopReason) {
        super.onStopService(stopReason)

        wifiDirectReceiver?.apply { unregisterReceiver(/* receiver = */ this) }

        val manager = wifiDirectManager ?: return
        val channel = wifiDirectChannel ?: return

        manager.removeGroup(channel, /* listener = */ null)
    }

    internal fun onPeerGroupUpdated(peerGroup: WifiP2pGroup) {
        // TODO: update information about peers
    }

    private fun isWiFiDirectSupported() = packageManager.hasSystemFeature(FEATURE_WIFI_DIRECT)

    @RequiresApi(api = 21)
    private fun isWiFiDirectEnabled(): Boolean {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as? WifiManager
        return wifiManager != null && wifiManager.isP2pSupported
    }

    @RequiresApi(api = 23)
    private fun isPermissionsGranted() = when {
        Build.VERSION.SDK_INT < 33 -> {
            checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
            checkSelfPermission(ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
        }

        else -> {
            checkSelfPermission(NEARBY_WIFI_DEVICES) == PERMISSION_GRANTED
        }
    }
}
