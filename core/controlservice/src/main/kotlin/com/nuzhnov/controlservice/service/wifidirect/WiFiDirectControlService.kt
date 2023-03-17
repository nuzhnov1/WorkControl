package com.nuzhnov.controlservice.service.wifidirect

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.NEARBY_WIFI_DEVICES
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.pm.PackageManager.FEATURE_WIFI_DIRECT
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.nuzhnov.controlservice.data.model.Role
import com.nuzhnov.controlservice.service.ControlService
import com.nuzhnov.controlservice.data.model.StopReason
import com.nuzhnov.controlservice.data.repository.WiFiDirectServiceRepository
import com.nuzhnov.controlservice.data.model.toStopReason
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: think about how to handling connection breaks

@AndroidEntryPoint
class WiFiDirectControlService : ControlService(),
    WifiP2pManager.GroupInfoListener,
    WifiP2pManager.ConnectionInfoListener {

    @Inject
    internal lateinit var injectedRepository: WiFiDirectServiceRepository
    override val repository get() = injectedRepository

    private var wifiDirectManager: WifiP2pManager? = null
    private var wifiDirectChannel: WifiP2pManager.Channel? = null
    private var wifiDirectReceiver: WiFiDirectBroadcastReceiver? = null

    private var thisDevice
        get() = repository.thisDevice.value
        set(value) = repository.updateThisDevice(value!!)

    private var peerGroup
        get() = repository.peerGroup.value
        set(value) = repository.updatePeerGroup(value!!)

    private var connectionInfo
        get() = repository.connectionInfo.value
        set(value) = repository.updateConnectionInfo(value!!)


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

        val channel = manager.initialize(
            /* srcContext = */ this,
            /* srcLooper = */ mainLooper,
            /* listener = */ null
        )
        if (channel == null) {
            onInitServiceFailed(StopReason.TECHNOLOGY_DISABLED)
            return
        } else {
            wifiDirectChannel = channel
        }

        wifiDirectReceiver = WiFiDirectBroadcastReceiver(
            wifiDirectService = this,
            wifiDirectManager = manager,
            wifiDirectChannel = channel
        )
    }

    override fun onStartService(startId: Int) {
        super.onStartService(startId)

        val wifiDirectReceiver = wifiDirectReceiver ?: return
        val receiverIntentFilter = IntentFilter().apply {
            WiFiDirectAction.values().forEach { action -> addAction(action.string) }
        }

        registerReceiver(wifiDirectReceiver, receiverIntentFilter)

        when (role) {
            Role.CONTROLLER -> createGroup()
            Role.CLIENT -> discoverPeers()
        }
    }

    override fun onStopService(stopReason: StopReason) {
        super.onStopService(stopReason)

        wifiDirectReceiver?.apply { unregisterReceiver(/* receiver = */ this) }

        val manager = wifiDirectManager ?: return
        val channel = wifiDirectChannel ?: return

        manager.removeGroup(channel, /* listener = */ null)
    }

    private fun createGroup() {
        val wifiDirectManager = wifiDirectManager ?: return
        val wifiDirectChannel = wifiDirectChannel ?: return

        wifiDirectManager.createGroup(wifiDirectChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() = Unit

            override fun onFailure(reason: Int) {
                // TODO: maybe recreate group N times with interval M seconds if reason is Busy
                onStopService(reason.toStopReason())
            }
        })
    }

    private fun discoverPeers() {
        val wifiDirectManager = wifiDirectManager ?: return
        val wifiDirectChannel = wifiDirectChannel ?: return

        wifiDirectManager.discoverPeers(wifiDirectChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                wifiDirectManager.discoverPeers(wifiDirectChannel, /* listener = */ this)
            }

            override fun onFailure(reason: Int) {
                val stopReason = reason.toStopReason()

                if (stopReason == StopReason.BUSY) {
                    wifiDirectManager.discoverPeers(wifiDirectChannel, /* listener = */ this)
                } else {
                    onStopService(reason.toStopReason())
                }
            }
        })
    }

    internal fun onThisDeviceUpdated(wifiP2pDevice: WifiP2pDevice) {
        thisDevice = wifiP2pDevice
    }

    override fun onConnectionInfoAvailable(wifiP2pInfo: WifiP2pInfo) {
        connectionInfo = wifiP2pInfo
    }

    override fun onGroupInfoAvailable(wifiP2pGroup: WifiP2pGroup) {
        peerGroup = wifiP2pGroup
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
