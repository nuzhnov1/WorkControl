package com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.notification

import com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.resources.studentServiceTitle
import com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.resources.toResourceString
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState.*
import com.nuzhnov.workcontrol.core.visitservice.studentservice.R
import com.nuzhnov.workcontrol.core.visitservice.notification.extensions.getContentPendingIntent
import com.nuzhnov.workcontrol.core.visitservice.notification.extensions.getNotificationBuilderTemplate
import com.nuzhnov.workcontrol.core.visitservice.notification.extensions.notifyIfPermissionGranted
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat

internal class StudentServiceNotificationManager(
    val applicationContext: Context,
    val notificationID: Int,
    contentActivityClass: Class<*>,
    notificationChannelID: String,
    initState: StudentServiceState
) {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val notificationBuilder = applicationContext
        .getNotificationBuilderTemplate(notificationChannelID)
        .setSmallIcon(R.drawable.student_service_small_icon_24)
        .setContentTitle(applicationContext.studentServiceTitle)
        .setContentIntent(applicationContext.getContentPendingIntent(contentActivityClass))

    var notification = initState.toNotification()
        private set


    fun updateNotification(state: StudentServiceState) {
        notification = state.toNotification()
        notificationManager.notifyIfPermissionGranted(
            applicationContext,
            notificationID,
            notification
        )
    }

    private fun StudentServiceState.toNotification(): Notification = when (this) {
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
