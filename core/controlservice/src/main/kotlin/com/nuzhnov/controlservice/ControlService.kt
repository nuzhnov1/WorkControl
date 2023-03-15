package com.nuzhnov.controlservice

import android.app.Service
import android.content.Intent
import android.os.Build
import com.nuzhnov.controlservice.data.ControlServiceRepository
import com.nuzhnov.controlservice.data.ControlServiceState.*
import com.nuzhnov.controlservice.data.StopReason
import javax.annotation.OverridingMethodsMustInvokeSuper

// TODO: implement the Notification

sealed class ControlService : Service() {

    protected abstract val repository: ControlServiceRepository


    final override fun onCreate() {
        onInitService()
    }

    final override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serviceState = repository.serviceState.value

        // If initialization failed
        if (serviceState is Stopped) {
            onStopService(serviceState.stopReason)
            return START_NOT_STICKY
        }

        if (serviceState !is Running) {
            onStartService(startId)
        }

        return START_STICKY
    }

    /**
     * This service doesn't support binding, so this method always returns null
     */
    final override fun onBind(intent: Intent?) = null

    final override fun onDestroy() {
        val serviceState = repository.serviceState.value

        if (serviceState is Initialized || serviceState is Running) {
            onStopService(StopReason.SERVICE_DESTROYED)
        }
    }

    @OverridingMethodsMustInvokeSuper
    open fun onInitService() {
         repository.setServiceState(Initialized)
    }

    @OverridingMethodsMustInvokeSuper
    open fun onInitServiceFailed(stopReason: StopReason) {
        repository.setServiceState(Stopped(stopReason))
    }

    @OverridingMethodsMustInvokeSuper
    open fun onStartService(startId: Int) {
        repository.setServiceState(Running)
        addToForeground(startId)
    }

    @OverridingMethodsMustInvokeSuper
    open fun onStopService(stopReason: StopReason) {
        repository.setServiceState(Stopped(stopReason))
        removeFromForeground()
        stopSelf()
    }

    private fun addToForeground(startId: Int) {
        // TODO: startForeground(...)
    }

    private fun removeFromForeground() {
        if (Build.VERSION.SDK_INT < 24) {
            @Suppress("DEPRECATION") stopForeground(/* removeNotification = */ true)
        } else {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }
}
