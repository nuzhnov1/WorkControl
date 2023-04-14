package com.nuzhnov.workcontrol.shared.visitservice.domen.service.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.util.createNotificationChannelGroup
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.ControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceInitFailedReason.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceError.*
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.ControlServiceCoroutineScope
import kotlinx.coroutines.*
import java.net.InetAddress
import javax.inject.Inject
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build

internal class NsdControlService : Service(), NsdManager.RegistrationListener {

    @Inject internal lateinit var repository: ControlServiceRepository
    @Inject @ControlServiceCoroutineScope internal lateinit var coroutineScope: CoroutineScope

    private var state: ControlServiceState
        get() = repository.serviceState.value
        set(value) = repository.updateServiceState(value)

    private var boundActivity: Class<*>? = null

    private var serviceName: String?
        get() = repository.serviceName.value
        set(value) = repository.updateServiceName(value)

    private var notificationManager: ControlServiceNotificationManager? = null
    private var controlJob: Job? = null
    private var nsdManager: NsdManager? = null


    override fun onCreate() {
        state = NotInitialized
        coroutineScope.launch {
            repository.serviceState.collect { state -> onStateChange(state) }
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
        notificationManager?.apply {
            updateNotification(newNotification = state.toNotification() ?: return)
        }
    }

    private fun onInit(intent: Intent?, startId: Int) {
        if (intent == null) {
            return
        }

        val activityClassNameExtra = intent.getStringExtra(BOUND_ACTIVITY_CLASS_NAME_EXTRA)!!
        val serviceNameExtra = intent.getStringExtra(SERVICE_NAME_EXTRA)!!
        val manager = getSystemService(NSD_SERVICE) as? NsdManager

        if (manager == null) {
            state = InitFailed(reason = TECHNOLOGY_UNAVAILABLE_ERROR)
            return
        }

        boundActivity = Class.forName(activityClassNameExtra)
        serviceName = serviceNameExtra
        nsdManager = manager
        initNotificationSystem(startId)
        state = ReadyToStart
    }

    private fun initNotificationSystem(startId: Int) {
        val boundActivity = boundActivity ?: return

        notificationManager = ControlServiceNotificationManager(
            context = applicationContext,
            notificationID = startId,
            activityClass = boundActivity
        ).apply {
            createNotificationChannelGroup()
            createControlServiceNotificationChannel()
            addToForeground(
                notificationID = startId,
                notification = NotInitialized.toNotification()!!
            )
        }
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

    private fun addToForeground(notificationID: Int, notification: Notification) {
        startForeground(notificationID, notification)
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

        const val BOUND_ACTIVITY_CLASS_NAME_EXTRA = "com.nuzhnov.workcontrol.shared. " +
                "visitservice.domen.service.control.boundActivityClassNameExtra"

        const val SERVICE_NAME_EXTRA = "com.nuzhnov.workcontrol.shared.visitservice.domen." +
                "service.control.serviceNameExtra"
    }
}
