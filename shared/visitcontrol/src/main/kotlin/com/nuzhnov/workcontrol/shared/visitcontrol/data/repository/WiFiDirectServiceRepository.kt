package com.nuzhnov.workcontrol.shared.visitcontrol.data.repository

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import com.nuzhnov.workcontrol.shared.visitcontrol.data.model.Role
import com.nuzhnov.workcontrol.shared.visitcontrol.di.annotations.ControlServiceCoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ServerSocket
import java.net.Socket
import javax.inject.Inject

class WiFiDirectServiceRepository @Inject constructor() : ServiceRepository() {

    @Inject
    @ControlServiceCoroutineScope
    internal lateinit var injectedCoroutineScope: CoroutineScope
    override val coroutineScope: CoroutineScope get() = injectedCoroutineScope

    private val _thisDevice = MutableStateFlow<WifiP2pDevice?>(value = null)
    val thisDevice = _thisDevice.asStateFlow()

    private val _peerGroup = MutableStateFlow<WifiP2pGroup?>(value = null)
    val peerGroup = _peerGroup.asStateFlow()

    private val _connectionInfo = MutableStateFlow<WifiP2pInfo?>(value = null)
    val connectionInfo = _connectionInfo.asStateFlow()

    private val mapMacAddressToID = mutableMapOf<String, Long>()


    internal fun updateThisDevice(thisDevice: WifiP2pDevice) {
        _thisDevice.value = thisDevice
    }

    internal fun updatePeerGroup(peerGroup: WifiP2pGroup) {
        _peerGroup.value = peerGroup

        val role = role.value

        if (role == Role.CONTROLLER) {
            peerGroup.clientList.forEach { wifiP2pDevice ->
                val macAddress = wifiP2pDevice.deviceAddress
                val uniqueID = mapMacAddressToID[macAddress]
                val isActiveNow = wifiP2pDevice.status == WifiP2pDevice.CONNECTED

                if (uniqueID != null) {
                    updateClient(uniqueID, isActiveNow)
                }
            }
        }
    }

    internal fun updateConnectionInfo(networkInfo: WifiP2pInfo) {
        _connectionInfo.value = networkInfo

        val role = role.value

        if (role == Role.CONTROLLER) {
            coroutineScope.launch(Dispatchers.IO) {
                // TODO: create server socket
                // TODO: listen connections
                // TODO: on receive connection - receive mac address and id, and update repository
            }
        } else {
            coroutineScope.launch {
                // TODO: connect to server using wifiP2pInfo
                // TODO: send mac address and id
            }
        }
    }

    override fun removeAllClients() {
        super.removeAllClients()
        mapMacAddressToID.clear()
    }
}
