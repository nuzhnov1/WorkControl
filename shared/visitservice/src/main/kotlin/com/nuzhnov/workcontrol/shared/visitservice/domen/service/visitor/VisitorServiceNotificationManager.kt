package com.nuzhnov.workcontrol.shared.visitservice.domen.service.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitorServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.ui.notification.*
import com.nuzhnov.workcontrol.shared.visitservice.ui.resources.*
import com.nuzhnov.workcontrol.shared.visitservice.R
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat

internal class VisitorServiceNotificationManager(
    val applicationContext: Context,
    val notificationChannelID: String,
    val notificationID: Int,
    val contentActivityClass: Class<*>,
    val initState: VisitorServiceState
) {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val notificationBuilder = applicationContext
        .getNotificationBuilderTemplate(notificationChannelID)
        .setSmallIcon(R.drawable.visitor_service_small_icon_24)
        .setContentTitle(applicationContext.visitorServiceTitle)
        .setContentIntent(applicationContext.getContentPendingIntent(contentActivityClass))

    var notification = initState.toNotification()
        private set


    fun updateNotification(state: VisitorServiceState) {
        notification = state.toNotification()
        notificationManager.notifyIfPermissionGranted(
            applicationContext,
            notificationID,
            notification
        )
    }

    private fun VisitorServiceState.toNotification(): Notification = when (this) {
        is Discovering, is Resolving, is Connecting, is Running -> notificationBuilder
            .setContentText(this.toResourceString(applicationContext))
            .setUsesChronometer(true)
            .setWhen(System.currentTimeMillis())

        else -> notificationBuilder
            .setContentText(this.toResourceString(applicationContext))
            .setUsesChronometer(false)
            .setWhen(System.currentTimeMillis())
    }.build()
}
