package com.nuzhnov.workcontrol.shared.visitservice.domen.service

import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceInitFailedReason.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceError.*
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.ControlServiceCoroutineScope
import kotlinx.coroutines.*
import java.net.InetAddress
import javax.inject.Inject
import android.app.Service
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build

internal class NsdControlService : Service(), NsdManager.RegistrationListener {

    @Inject internal lateinit var repository: ControlServiceRepository
    @Inject @ControlServiceCoroutineScope internal lateinit var coroutineScope: CoroutineScope

    private var state: ControlServiceState
        get() = repository.state.value
        set(value) = repository.updateState(value)

    private var serviceName: String?
        get() = repository.name.value
        set(value) = repository.updateName(value)

    private var controlJob: Job? = null
    private var nsdManager: NsdManager? = null


    override fun onCreate() {
        coroutineScope.launch {
            repository.state.collect { state -> onStateChange(state) }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (state is NotInitialized) {
            onInit(intent, startId)
        }

        if (state is Stopped || state is StoppedByError) {
            state = ReadyToStart
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null

    override fun onDestroy() {
        nsdManager?.unregisterService(/* listener = */ this)
        coroutineScope.cancel()
        serviceName = null

        removeFromForeground()
    }

    private fun onStateChange(state: ControlServiceState) {
        onNotificationChange(state)

        when (state) {
            is ReadyToStart -> onStart()
            is Running -> onRegisterService(state.serverAddress, state.serverPort)
            is Stopped, is StoppedByError -> onStop()
            else -> Unit
        }
    }

    private fun onNotificationChange(state: ControlServiceState) {
        // TODO: implemented notifications about the service state changes
    }

    private fun onInit(intent: Intent?, startId: Int) {
        addToForeground(startId)

        if (intent == null) {
            state = InitFailed(reason = INTENT_MISSED)
            return
        }

        when (val serviceNameExtra = intent.getStringExtra(SERVICE_NAME_EXTRA)) {
            null -> {
                state = InitFailed(reason = SERVICE_NAME_EXTRA_MISSED)
                return
            }

            else -> serviceName = serviceNameExtra
        }

        when (val manager = getSystemService(NSD_SERVICE) as? NsdManager) {
            null -> {
                state = InitFailed(reason = TECHNOLOGY_UNAVAILABLE_ERROR)
                return
            }

            else -> nsdManager = manager
        }

        state = ReadyToStart
    }

    private fun onStart() {
        controlJob?.cancel()
        controlJob = coroutineScope.launch {
            repository.startControl()
        }
    }

    private fun onRegisterService(serverAddress: InetAddress, serverPort: Int) {
        val nsdManager = nsdManager
        val nsdServiceName = this@NsdControlService.serviceName

        if (nsdServiceName == null || nsdManager == null) {
            onRegistrationFailed()
            return
        }

        val nsdServiceInfo = NsdServiceInfo().apply {
            serviceName = nsdServiceName
            serviceType = SERVICE_TYPE
            host = serverAddress
            port = serverPort
        }

        nsdManager.registerService(
            /* serviceInfo = */ nsdServiceInfo,
            /* protocolType = */ NsdManager.PROTOCOL_DNS_SD,
            /* listener = */ this
        )
    }

    override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo?) {
        serviceName = nsdServiceInfo?.serviceName ?: return
    }

    override fun onRegistrationFailed(nsdServiceInfo: NsdServiceInfo?, errorCode: Int) {
        onRegistrationFailed()
    }

    private fun onRegistrationFailed() {
        state = StoppedByError(error = REGISTER_SERVICE_FAILED)
        controlJob?.cancel()
    }

    override fun onServiceUnregistered(nsdServiceInfo: NsdServiceInfo?) = Unit
    override fun onUnregistrationFailed(nsdServiceInfo: NsdServiceInfo?, errorCode: Int) = Unit

    private fun onStop() {
        nsdManager?.unregisterService(/* listener = */ this)
    }

    private fun addToForeground(startId: Int) {
        // TODO: startForeground(startId, ...)
    }

    private fun removeFromForeground() {
        if (Build.VERSION.SDK_INT < 24) {
            @Suppress("DEPRECATION") stopForeground(/* removeNotification = */ true)
        } else {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }


    companion object {
        const val SERVICE_TYPE = "_vctrl._tcp"

        const val SERVICE_NAME_EXTRA = "com.nuzhnov.workcontrol.shared.visitservice.domen." +
                "service.serviceNameExtra"
    }
}
