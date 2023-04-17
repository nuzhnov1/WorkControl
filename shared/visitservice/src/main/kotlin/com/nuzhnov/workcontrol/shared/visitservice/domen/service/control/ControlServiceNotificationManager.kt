package com.nuzhnov.workcontrol.shared.visitservice.domen.service.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.ui.notification.*
import com.nuzhnov.workcontrol.shared.visitservice.ui.resources.*
import com.nuzhnov.workcontrol.shared.visitservice.R
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat

internal class ControlServiceNotificationManager(
    val applicationContext: Context,
    val notificationChannelID: String,
    val notificationID: Int,
    val contentActivityClass: Class<*>,
    val initState: ControlServiceState
) {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val notificationBuilder = applicationContext
        .getNotificationBuilderTemplate(notificationChannelID)
        .setSmallIcon(R.drawable.visitor_service_small_icon_24)
        .setContentTitle(applicationContext.controlServiceTitle)
        .setContentIntent(applicationContext.getContentPendingIntent(contentActivityClass))

    var notification = initState.toNotification()
        private set


    fun updateNotification(state: ControlServiceState) {
        notification = state.toNotification()
        notificationManager.notifyIfPermissionGranted(
            applicationContext,
            notificationID,
            notification
        )
    }

    private fun ControlServiceState.toNotification(): Notification = when (this) {
        is Running -> notificationBuilder
            .setContentText(this.toResourceString(applicationContext))
            .setUsesChronometer(true)
            .setWhen(System.currentTimeMillis())

        is StoppedAcceptConnections -> notificationBuilder
            .setContentText(this.toResourceString(applicationContext))
            .setUsesChronometer(true)
            .setWhen(notification.`when`)

        else -> notificationBuilder
            .setContentText(this.toResourceString(applicationContext))
            .setUsesChronometer(false)
            .setWhen(System.currentTimeMillis())
    }.build()
}
