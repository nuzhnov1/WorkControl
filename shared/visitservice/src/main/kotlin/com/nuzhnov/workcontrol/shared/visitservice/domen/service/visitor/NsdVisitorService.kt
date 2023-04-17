package com.nuzhnov.workcontrol.shared.visitservice.domen.service.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.visitor.VisitorServiceCommand.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.control.NsdControlService
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceError.*
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitorServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitservice.util.getSerializable
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import kotlinx.coroutines.*
import javax.inject.Inject
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build

internal class NsdVisitorService : Service(),
    NsdManager.DiscoveryListener,
    NsdManager.ResolveListener {

    @Inject internal lateinit var repository: VisitorServiceRepository
    @Inject @VisitorServiceCoroutineScope internal lateinit var coroutineScope: CoroutineScope

    private var state: VisitorServiceState
        get() = repository.serviceState.value
        set(value) = repository.updateServiceState(value)

    private var visitorID: VisitorID? = null
    private var executedCommand: VisitorServiceCommand? = null

    private var notificationManager: VisitorServiceNotificationManager? = null
    private var visitorJob: Job? = null
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

        if (state is InitFailed) {
            return START_STICKY
        }

        cancelCurrentJob()
        executedCommand = intent?.getCommandExtra()

        when (val command = executedCommand) {
            null -> Unit
            is Discover -> state = Discovering
            is Connect -> state = Resolving(command.serviceName)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null

    override fun onDestroy() {
        repository.clearDiscoveredServices()
        nsdManager?.stopServiceDiscovery(/* listener = */ this)
        coroutineScope.cancel()

        removeFromForeground()
    }

    private fun onStateChange(state: VisitorServiceState) {
        onNotificationChange(state)

        when (state) {
            is Discovering -> onDiscover()
            is Resolving -> onResolve(state.serviceName)
            is Stopped, is StoppedByError -> onStop()
            else -> Unit
        }
    }

    private fun onNotificationChange(state: VisitorServiceState) {
        notificationManager?.updateNotification(state)
    }

    private fun onInit(intent: Intent?, startId: Int) {
        if (intent == null) {
            return
        }

        val activityClassNameExtra = intent.getStringExtra(CONTENT_ACTIVITY_CLASS_NAME_EXTRA)!!
        val contentActivityClass = Class.forName(activityClassNameExtra)
        val visitorIdExtra = intent.getLongExtra(VISITOR_ID_EXTRA, /* defaultValue = */ 0)
        val notificationChannelID = intent.getStringExtra(NOTIFICATION_CHANNEL_ID_EXTRA)!!
        val manager = getSystemService(NSD_SERVICE) as? NsdManager

        initNotificationSystem(
            notificationID = startId,
            notificationChannelID = notificationChannelID,
            contentActivityClass = contentActivityClass
        )

        if (manager == null) {
            state = InitFailed
            return
        }

        visitorID = visitorIdExtra
        nsdManager = manager
        state = ReadyToRun
    }

    private fun initNotificationSystem(
        notificationID: Int,
        notificationChannelID: String,
        contentActivityClass: Class<*>
    ) {
        notificationManager = VisitorServiceNotificationManager(
            applicationContext = applicationContext,
            notificationChannelID = notificationChannelID,
            notificationID = notificationID,
            contentActivityClass = contentActivityClass,
            initState = state
        ).apply { addToForeground(notificationID, notification) }
    }

    private fun Intent.getCommandExtra() = getSerializable<VisitorServiceCommand>(COMMAND_EXTRA)!!

    private fun onDiscover() {
        val nsdManager = nsdManager

        if (nsdManager == null) {
            onDiscoverFailed()
            return
        }

        nsdManager.discoverServices(
            /* serviceType = */ NsdControlService.SERVICE_TYPE,
            /* protocolType = */ NsdManager.PROTOCOL_DNS_SD,
            /* listener = */ this
        )
    }

    override fun onDiscoveryStarted(serviceType: String?) = Unit

    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
        onDiscoverFailed()
    }

    private fun onDiscoverFailed() {
        repository.clearDiscoveredServices()
        nsdManager?.stopServiceDiscovery(/* listener = */ this)
        state = StoppedByError(error = DISCOVER_SERVICES_FAILED)
    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        if (serviceInfo != null) {
            repository.addDiscoveredService(serviceInfo)
        }
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
        if (serviceInfo != null) {
            repository.removeDiscoveredService(serviceInfo)
        }
    }

    override fun onDiscoveryStopped(serviceType: String?) {
        repository.clearDiscoveredServices()
    }

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) = Unit

    private fun onResolve(serviceName: String) {
        val nsdManager = nsdManager
        val discoveredServices = repository.getDiscoveredServices()
        val nsdServiceInfo = discoveredServices[serviceName]

        if (nsdManager == null || nsdServiceInfo == null) {
            onResolveFailed()
            return
        }

        nsdManager.resolveService(nsdServiceInfo,/* listener = */ this)
    }

    override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
        val visitorID = visitorID

        when {
            serviceInfo == null -> onResolveFailed()
            visitorID == null -> return

            else -> {
                val serverAddress = serviceInfo.host
                val serverPort = serviceInfo.port

                visitorJob?.cancel()
                visitorJob = coroutineScope.launch {
                    repository.startVisit(serverAddress, serverPort, visitorID)
                }
            }
        }
    }

    override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        onResolveFailed()
    }

    private fun onResolveFailed() {
        state = StoppedByError(error = RESOLVE_SERVICE_FAILED)
    }

    private fun onStop() {
        executedCommand = null
    }

    private fun cancelCurrentJob() {
        when (executedCommand) {
            null -> Unit
            is Discover -> nsdManager?.stopServiceDiscovery(/* listener = */ this)
            is Connect -> visitorJob?.cancel()
        }

        executedCommand = null
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
        internal const val CONTENT_ACTIVITY_CLASS_NAME_EXTRA = "com.nuzhnov.workcontrol.shared" +
                ".visitservice.domen.service.visitor.CONTENT_ACTIVITY_CLASS_NAME_EXTRA"

        internal const val VISITOR_ID_EXTRA = "com.nuzhnov.workcontrol.shared" +
                ".visitservice.domen.service.visitor.VISITOR_ID_EXTRA"

        internal const val COMMAND_EXTRA = "com.nuzhnov.workcontrol.shared" +
                ".visitservice.domen.service.visitor.COMMAND_EXTRA"

        internal const val NOTIFICATION_CHANNEL_ID_EXTRA = "com.nuzhnov.workcontrol.shared" +
                ".visitservice.domen.service.visitor.NOTIFICATION_CHANNEL_ID_EXTRA"
    }
}
