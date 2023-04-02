package com.nuzhnov.workcontrol.shared.visitservice.domen.service

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceInitFailedReason
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceInitFailedReason.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceError
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceError.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.repository.VisitControlServiceRepository
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.VisitControlServiceCoroutineScope
import kotlinx.coroutines.*
import java.net.InetAddress
import javax.annotation.OverridingMethodsMustInvokeSuper
import javax.inject.Inject
import android.app.Service
import android.content.Intent
import android.os.Build

internal abstract class VisitControlService : Service() {

    @Inject internal lateinit var repository: VisitControlServiceRepository
    @Inject @VisitControlServiceCoroutineScope internal lateinit var coroutineScope: CoroutineScope

    private var state: VisitControlServiceState
        get() = repository.serviceState.value
        set(value) = repository.updateServiceState(value)

    private var controlServerJob: Job? = null
    protected lateinit var serviceName: String


    final override fun onCreate() {
        state = NotInitialized

        coroutineScope.launch {
            repository.serviceState.collect { serviceState -> onStateChange(serviceState) }
        }
    }

    final override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (state) {
            is NotInitialized -> state = Initializing(intent, startId)
            is Stopped, is StoppedByError -> state = Initialized
            else -> Unit
        }

        return START_STICKY
    }

    final override fun onBind(intent: Intent?) = null

    final override fun onDestroy() {
        coroutineScope.cancel()
        removeFromForeground()
    }

    private fun onStateChange(serviceState: VisitControlServiceState) {
        onNotificationChange(serviceState)

        when (serviceState) {
            is NotInitialized -> Unit
            is Initializing -> onInit(serviceState.intent, serviceState.startId)
            is InitFailed -> onInitFailed(serviceState.reason)
            is Initialized -> onStart()
            is NotRunning -> Unit
            is Running -> onRun(serviceState.serverAddress, serviceState.serverPort)

            is StoppedAcceptConnections -> onStopAcceptConnections(
                serviceState.serverAddress,
                serviceState.serverPort,
                serviceState.error
            )

            is Stopped -> onStop()
            is StoppedByError -> onStop(serviceState.error)
        }
    }

    private fun onNotificationChange(serviceState: VisitControlServiceState) {
        // TODO: implemented notifications about the service state changes
    }

    private fun onInit(intent: Intent?, startId: Int) {
        addToForeground(startId)
        state = when (val fetchStatus = onFetchIntentExtras(intent)) {
            is InitStatus.Failed -> InitFailed(fetchStatus.reason)

            else -> when (val initStatus = onInit()) {
                is InitStatus.Success -> Initialized
                is InitStatus.Failed -> InitFailed(initStatus.reason)
            }
        }
    }

    private fun onFetchIntentExtras(intentOrNull: Intent?): InitStatus {
        val intent = when (intentOrNull) {
            null -> return InitStatus.Failed(reason = SERVICE_INTENT_MISSED)
            else -> intentOrNull
        }

        when (val serviceNameExtra = intent.getStringExtra(SERVICE_NAME_EXTRA)) {
            null -> return InitStatus.Failed(reason = SERVICE_NAME_EXTRA_MISSED)
            else -> serviceName = serviceNameExtra
        }

        return InitStatus.Success
    }

    protected abstract fun onInit(): InitStatus

    protected open fun onInitFailed(reason: VisitControlServiceInitFailedReason) = Unit

    @OverridingMethodsMustInvokeSuper
    protected open fun onStart() {
        if (controlServerJob == null) {
            controlServerJob = coroutineScope.launch {
                repository.startVisitControlServer()
            }
        }
    }

    protected open fun onRun(serverAddress: InetAddress, serverPort: Int) = Unit

    protected open fun onStopAcceptConnections(
        serverAddress: InetAddress,
        serverPort: Int,
        error: VisitControlServiceError
    ) = Unit

    @OverridingMethodsMustInvokeSuper
    protected open fun onStop(error: VisitControlServiceError? = null) {
        controlServerJob = null
    }

    @OverridingMethodsMustInvokeSuper
    protected open fun onCreateNetworkFailed() {
        controlServerJob?.cancel()
        controlServerJob = null
        state = StoppedByError(error = CREATE_NETWORK_FAILED)
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
        const val SERVICE_NAME_EXTRA = "com.nuzhnov.workcontrol.shared.visitservice.domen." +
                "service.serviceNameExtra"
    }

    sealed interface InitStatus {
        object Success : InitStatus
        data class Failed(val reason: VisitControlServiceInitFailedReason) : InitStatus
    }
}
