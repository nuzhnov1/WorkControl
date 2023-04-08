package com.nuzhnov.workcontrol.shared.visitservice.domen.service

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.model.VisitorServiceCommand
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.model.VisitorServiceCommand.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitorServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceInitFailedReason.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceError.*
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitorServiceCoroutineScope
import com.nuzhnov.workcontrol.shared.visitservice.util.getSerializable
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import kotlinx.coroutines.*
import javax.inject.Inject
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

    private var visitorJob: Job? = null
    private var executedCommand: VisitorServiceCommand? = null
    private var nsdManager: NsdManager? = null
    private val discoveredServices = mutableMapOf<String, NsdServiceInfo>()


    override fun onCreate() {
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
        clearDiscoveredServices()
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

    private fun onNotificationChange(serviceState: VisitorServiceState) {
        // TODO: implemented notifications about the service state changes
    }

    private fun onInit(intent: Intent?, startId: Int) {
        addToForeground(startId)

        if (intent == null) {
            state = InitFailed(reason = INTENT_MISSED)
            return
        }

        when (val visitorIdExtra = intent.getLongExtra(VISITOR_ID_EXTRA, INVALID_ID)) {
            INVALID_ID -> {
                state = InitFailed(reason = VISITOR_ID_MISSED)
                return
            }

            else -> visitorID = visitorIdExtra
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

    private fun Intent.getCommandExtra() = getSerializable<VisitorServiceCommand>(COMMAND_EXTRA)

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
        clearDiscoveredServices()
        nsdManager?.stopServiceDiscovery(/* listener = */ this)
        state = StoppedByError(error = DISCOVER_SERVICE_FAILED)
    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        if (serviceInfo != null) {
            addDiscoveredService(serviceInfo)
        }
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
        if (serviceInfo != null) {
            removeDiscoveredService(serviceInfo)
        }
    }

    override fun onDiscoveryStopped(serviceType: String?) {
        clearDiscoveredServices()
    }

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) = Unit

    private fun onResolve(serviceName: String) {
        val nsdManager = nsdManager
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
        visitorJob?.cancel()
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

    private fun addDiscoveredService(serviceInfo: NsdServiceInfo) {
        val serviceName = serviceInfo.serviceName

        discoveredServices[serviceName] = serviceInfo
        repository.addDiscoveredService(serviceName)
    }

    private fun removeDiscoveredService(serviceInfo: NsdServiceInfo) {
        val serviceName = serviceInfo.serviceName

        discoveredServices.remove(serviceName)
        repository.removeDiscoveredService(serviceName)
    }

    private fun clearDiscoveredServices() {
        discoveredServices.clear()
        repository.clearDiscoveredServices()
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
        const val INVALID_ID = VisitorID.MIN_VALUE

        const val VISITOR_ID_EXTRA = "com.nuzhnov.workcontrol.shared.visitservice.domen." +
                "service.visitorID"

        const val COMMAND_EXTRA = "com.nuzhnov.workcontrol.shared.visitservice.domen." +
                "service.commandExtra"
    }
}
