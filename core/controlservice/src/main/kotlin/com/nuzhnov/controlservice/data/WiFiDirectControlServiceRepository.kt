package com.nuzhnov.controlservice.data

import android.net.wifi.p2p.WifiP2pDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class WiFiDirectControlServiceRepository @Inject constructor() : ControlServiceRepository() {

    private val _wifiP2pDevice = MutableStateFlow<WifiP2pDevice?>(value = null)
    val wifiDirectDevice = _wifiP2pDevice.asStateFlow()


    internal fun setThisDevice(wifiP2pDevice: WifiP2pDevice?) {
        _wifiP2pDevice.value = wifiP2pDevice
    }
}
