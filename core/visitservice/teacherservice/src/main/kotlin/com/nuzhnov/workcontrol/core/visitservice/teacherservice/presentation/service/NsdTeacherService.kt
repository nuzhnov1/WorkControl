package com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.service

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.notification.TeacherServiceNotificationManager
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.GetTeacherServiceStateUseCase
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.GetTeacherServiceNameUseCase
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal.StartControlUseCase
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal.UpdateTeacherServiceStateUseCase
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal.UpdateTeacherServiceNameUseCase
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState.*
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceError.*
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.di.annotation.TeacherServiceCoroutineScope
import com.nuzhnov.workcontrol.core.visitservice.util.constant.VISIT_CONTROL_PROTOCOL_NAME
import kotlinx.coroutines.*
import java.net.InetAddress
import javax.inject.Inject
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class NsdTeacherService : Service(), NsdManager.RegistrationListener {

    @Inject
    internal lateinit var getTeacherServiceStateUseCase: GetTeacherServiceStateUseCase
    @Inject
    internal lateinit var getTeacherServiceNameUseCase: GetTeacherServiceNameUseCase
    @Inject
    internal lateinit var updateTeacherServiceStateUseCase: UpdateTeacherServiceStateUseCase
    @Inject
    internal lateinit var updateTeacherServiceNameUseCase: UpdateTeacherServiceNameUseCase
    @Inject
    internal lateinit var startControlUseCase: StartControlUseCase
    @[Inject TeacherServiceCoroutineScope]
    internal lateinit var coroutineScope: CoroutineScope

    private var state: TeacherServiceState
        get() = getTeacherServiceStateUseCase().value
        set(value) = updateTeacherServiceStateUseCase(serviceState = value)

    private var serviceName: String?
        get() = getTeacherServiceNameUseCase().value
        set(value) = updateTeacherServiceNameUseCase(value)

    private var notificationManager: TeacherServiceNotificationManager? = null
    private var controlJob: Job? = null
    private var nsdManager: NsdManager? = null


    override fun onCreate() {
        state = NotInitialized
        getTeacherServiceStateUseCase()
            .onEach { state -> onStateChange(state) }
            .launchIn(scope = coroutineScope)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (state is NotInitialized || state is Stopped || state is StoppedByError) {
            onInit(intent, startId)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null

    override fun onDestroy() {
        nsdManager?.unregisterService(/* listener = */ this)
        coroutineScope.cancel()

        removeFromForeground()
    }

    private fun onStateChange(state: TeacherServiceState) {
        onNotificationChange(state)

        when (state) {
            is ReadyToRun -> onStart()
            is Running -> onRegisterService(state.serverAddress, state.serverPort)
            is Stopped, is StoppedByError -> onStop()
            else -> Unit
        }
    }

    private fun onNotificationChange(state: TeacherServiceState) {
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
        notificationManager = TeacherServiceNotificationManager(
            applicationContext = applicationContext,
            notificationID = notificationID,
            contentActivityClass = contentActivityClass,
            notificationChannelID = notificationChannelID,
            initState = state
        ).apply { addToForeground(notificationID, notification) }
    }

    private fun onStart() {
        controlJob?.cancel()
        controlJob = coroutineScope.launch { startControlUseCase() }
    }

    private fun onRegisterService(serverAddress: InetAddress, serverPort: Int) {
        val nsdManager = nsdManager
        val nsdServiceName = serviceName

        if (nsdServiceName == null || nsdManager == null) {
            onRegistrationFailed()
            return
        }

        val nsdServiceInfo = NsdServiceInfo().apply {
            serviceName = nsdServiceName
            serviceType = VISIT_CONTROL_PROTOCOL_NAME
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


    internal companion object {
        const val CONTENT_ACTIVITY_CLASS_NAME_EXTRA = "com.nuzhnov.workcontrol.core.visitservice" +
                ".teacherservice.CONTENT_ACTIVITY_CLASS_NAME_EXTRA"

        const val NOTIFICATION_CHANNEL_ID_EXTRA = "com.nuzhnov.workcontrol.core.visitservice" +
                ".teacherservice.NOTIFICATION_CHANNEL_ID_EXTRA"
    }
}
