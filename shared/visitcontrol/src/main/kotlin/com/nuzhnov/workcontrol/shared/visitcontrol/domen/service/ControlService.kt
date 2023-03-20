package com.nuzhnov.workcontrol.shared.visitcontrol.domen.service

import android.app.Service
import android.content.Intent
import android.os.Build
import com.nuzhnov.workcontrol.shared.visitcontrol.data.model.Role
import com.nuzhnov.workcontrol.shared.visitcontrol.data.model.ServiceState
import com.nuzhnov.workcontrol.shared.visitcontrol.data.repository.ServiceRepository
import com.nuzhnov.workcontrol.shared.visitcontrol.data.model.ServiceState.*
import com.nuzhnov.workcontrol.shared.visitcontrol.data.model.StopReason
import com.nuzhnov.workcontrol.shared.visitcontrol.domen.service.wifidirect.getSerializable
import javax.annotation.OverridingMethodsMustInvokeSuper

// TODO: implement user notifications about service status changes

abstract class ControlService : Service() {

    protected abstract val repository: ServiceRepository

    protected var role: Role
        get() = repository.role.value!!
        set(value) = repository.setRole(value)

    private var state: ServiceState
        get() = repository.serviceState.value!!
        set(value) = repository.updateServiceState(value)

    protected val clients get() = repository.clients.value


    final override fun onCreate() {
        state = Initialized
        onInitService()
    }

    final override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serviceState = state

        // If initialization failed
        if (serviceState is Stopped) {
            onStopService(serviceState.stopReason)
            return START_NOT_STICKY
        }

        if (serviceState !is Running) {
            role = intent?.getSerializable(EXTRA_ROLE) ?: Role.CLIENT
            onStartService(startId)
        }

        return START_STICKY
    }

    /**
     * This service doesn't support binding, so this method always returns null
     */
    final override fun onBind(intent: Intent?) = null

    final override fun onDestroy() {
        val serviceState = state

        if (serviceState is Initialized || serviceState is Running) {
            onStopService(StopReason.SERVICE_DESTROYED)
        }
    }

    open fun onInitService() = Unit

    @OverridingMethodsMustInvokeSuper
    open fun onInitServiceFailed(stopReason: StopReason) {
        state = Stopped(stopReason)
    }

    @OverridingMethodsMustInvokeSuper
    open fun onStartService(startId: Int) {
        state = Running
        addToForeground(startId)
    }

    @OverridingMethodsMustInvokeSuper
    open fun onStopService(stopReason: StopReason) {
        state = Stopped(stopReason)
        repository.removeAllClients()
        removeFromForeground()
        stopSelf()
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
        const val EXTRA_ROLE = "extraRole"
    }
}
