package com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.service

import com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.notification.StudentServiceNotificationManager
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.GetStudentServiceStateUseCase
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.internal.*
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState.*
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceError.*
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceCommand
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceCommand.*
import com.nuzhnov.workcontrol.core.visitservice.studentservice.di.annotation.StudentServiceCoroutineScope
import com.nuzhnov.workcontrol.core.visitservice.util.constant.VISIT_CONTROL_PROTOCOL_NAME
import com.nuzhnov.workcontrol.core.visitservice.util.extensions.getSerializable
import kotlinx.coroutines.*
import javax.inject.Inject
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build

internal class NsdStudentService : Service(),
    NsdManager.DiscoveryListener,
    NsdManager.ResolveListener {

    @Inject internal lateinit var getStudentServiceStateUseCase: GetStudentServiceStateUseCase
    @Inject internal lateinit var getNsdDiscoveredServicesMapUseCase: GetNsdDiscoveredServicesMapUseCase
    @Inject internal lateinit var updateStudentServiceStateUseCase: UpdateStudentServiceStateUseCase
    @Inject internal lateinit var addNsdDiscoveredServiceUseCase: AddNsdDiscoveredServiceUseCase
    @Inject internal lateinit var removeNsdDiscoveredServiceUseCase: RemoveNsdDiscoveredServiceUseCase
    @Inject internal lateinit var clearNsdDiscoveredServicesUseCase: ClearNsdDiscoveredServicesUseCase
    @Inject internal lateinit var startVisitUseCase: StartVisitUseCase
    @[Inject StudentServiceCoroutineScope] internal lateinit var coroutineScope: CoroutineScope

    private var state: StudentServiceState
        get() = getStudentServiceStateUseCase().value
        set(value) = updateStudentServiceStateUseCase(serviceState = value)

    private var executedCommand: StudentServiceCommand? = null

    private var notificationManager: StudentServiceNotificationManager? = null
    private var visitorJob: Job? = null
    private var nsdManager: NsdManager? = null


    override fun onCreate() {
        state = NotInitialized
        coroutineScope.launch {
            getStudentServiceStateUseCase().collect { state -> onStateChange(state) }
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
        nsdManager?.stopServiceDiscovery(/* listener = */ this)
        coroutineScope.cancel()

        removeFromForeground()
    }

    private fun onStateChange(state: StudentServiceState) {
        onNotificationChange(state)

        when (state) {
            is Discovering -> onDiscover()
            is Resolving -> onResolve(state.serviceName)
            is Stopped, is StoppedByError -> onStop()
            else -> Unit
        }
    }

    private fun onNotificationChange(state: StudentServiceState) {
        notificationManager?.updateNotification(state)
    }

    private fun onInit(intent: Intent?, startId: Int) {
        if (intent == null) {
            return
        }

        val activityClassNameExtra = intent.getStringExtra(CONTENT_ACTIVITY_CLASS_NAME_EXTRA)!!
        val contentActivityClass = Class.forName(activityClassNameExtra)
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

        nsdManager = manager
        state = ReadyToRun
    }

    private fun initNotificationSystem(
        notificationID: Int,
        notificationChannelID: String,
        contentActivityClass: Class<*>
    ) {
        notificationManager = StudentServiceNotificationManager(
            applicationContext = applicationContext,
            notificationChannelID = notificationChannelID,
            notificationID = notificationID,
            contentActivityClass = contentActivityClass,
            initState = state
        ).apply { addToForeground(notificationID, notification) }
    }

    private fun Intent.getCommandExtra() = getSerializable<StudentServiceCommand>(COMMAND_EXTRA)!!

    private fun onDiscover() {
        val nsdManager = nsdManager

        if (nsdManager == null) {
            onDiscoverFailed()
            return
        }

        nsdManager.discoverServices(
            /* serviceType = */ VISIT_CONTROL_PROTOCOL_NAME,
            /* protocolType = */ NsdManager.PROTOCOL_DNS_SD,
            /* listener = */ this
        )
    }

    override fun onDiscoveryStarted(serviceType: String?) {
        clearNsdDiscoveredServicesUseCase()
    }

    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
        onDiscoverFailed()
    }

    private fun onDiscoverFailed() {
        nsdManager?.stopServiceDiscovery(/* listener = */ this)
        state = StoppedByError(error = DISCOVER_SERVICES_FAILED)
    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        if (serviceInfo != null) {
            addNsdDiscoveredServiceUseCase(nsdService = serviceInfo)
        }
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
        if (serviceInfo != null) {
            removeNsdDiscoveredServiceUseCase(nsdService = serviceInfo)
        }
    }

    override fun onDiscoveryStopped(serviceType: String?) = Unit

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) = Unit

    private fun onResolve(serviceName: String) {
        val nsdManager = nsdManager
        val discoveredServices = getNsdDiscoveredServicesMapUseCase()
        val nsdServiceInfo = discoveredServices[serviceName]

        if (nsdManager == null || nsdServiceInfo == null) {
            onResolveFailed()
            return
        }

        nsdManager.resolveService(nsdServiceInfo,/* listener = */ this)
    }

    override fun onServiceResolved(serviceInfo: NsdServiceInfo?): Unit = when (serviceInfo) {
        null -> onResolveFailed()

        else -> {
            val serverAddress = serviceInfo.host
            val serverPort = serviceInfo.port

            visitorJob?.cancel()
            visitorJob = coroutineScope.launch {
                startVisitUseCase(serverAddress, serverPort)
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


    internal companion object {
        const val COMMAND_EXTRA = "com.nuzhnov.workcontrol.core.visitservice" +
                ".studentservice.presentation.service.COMMAND_EXTRA"

        const val CONTENT_ACTIVITY_CLASS_NAME_EXTRA = "com.nuzhnov.workcontrol.core.visitservice" +
                ".studentservice.presentation.service.CONTENT_ACTIVITY_CLASS_NAME_EXTRA"

        const val NOTIFICATION_CHANNEL_ID_EXTRA = "com.nuzhnov.workcontrol.core.visitservice" +
                ".studentservice.presentation.service.NOTIFICATION_CHANNEL_ID_EXTRA"
    }
}
