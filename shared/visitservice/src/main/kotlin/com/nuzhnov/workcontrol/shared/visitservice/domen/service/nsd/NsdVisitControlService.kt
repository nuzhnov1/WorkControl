package com.nuzhnov.workcontrol.shared.visitservice.domen.service.nsd

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceInitFailedReason.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.VisitControlService
import java.net.InetAddress
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceError

internal class NsdVisitControlService : VisitControlService() {

    private lateinit var nsdManager: NsdManager
    private val registrationListener = object : RegistrationListener {
        override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo) = Unit

        override fun onRegistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
            onCreateNetworkFailed()
        }

        override fun onServiceUnregistered(nsdServiceInfo: NsdServiceInfo) = Unit

        override fun onUnregistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) = Unit
    }


    override fun onInit(): InitStatus {
        val manager = getSystemService(NSD_SERVICE) as? NsdManager

        if (manager == null) {
            return InitStatus.Failed(reason = TECHNOLOGY_DISABLED_ERROR)
        } else {
            nsdManager = manager
        }

        return InitStatus.Success
    }

    override fun onRun(serverAddress: InetAddress, serverPort: Int) {
        val nsdServiceInfo = NsdServiceInfo().apply {
            val nsdServiceName = this@NsdVisitControlService.serviceName

            serviceName = nsdServiceName
            serviceType = "_${nsdServiceName.lowercase()}._tcp"
            host = serverAddress
            port = serverPort
        }

        nsdManager.registerService(
            /* serviceInfo = */ nsdServiceInfo,
            /* protocolType = */ NsdManager.PROTOCOL_DNS_SD,
            /* listener = */ registrationListener
        )
    }

    override fun onStop(error: VisitControlServiceError?) {
        super.onStop(error)
        nsdManager.unregisterService(registrationListener)
    }
}
