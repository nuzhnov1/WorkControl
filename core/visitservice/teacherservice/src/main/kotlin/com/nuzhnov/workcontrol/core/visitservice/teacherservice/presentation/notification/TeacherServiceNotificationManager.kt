package com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.notification

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.resources.*
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState.*
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.R
import com.nuzhnov.workcontrol.core.visitservice.notification.getContentPendingIntent
import com.nuzhnov.workcontrol.core.visitservice.notification.getNotificationBuilderTemplate
import com.nuzhnov.workcontrol.core.visitservice.notification.notifyIfPermissionGranted
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat

internal class TeacherServiceNotificationManager(
    val applicationContext: Context,
    val notificationID: Int,
    contentActivityClass: Class<*>,
    notificationChannelID: String,
    initState: TeacherServiceState
) {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val notificationBuilder = applicationContext
        .getNotificationBuilderTemplate(notificationChannelID)
        .setSmallIcon(R.drawable.teacher_service_small_icon_24)
        .setContentTitle(applicationContext.teacherServiceTitle)
        .setContentIntent(applicationContext.getContentPendingIntent(contentActivityClass))

    var notification = initState.toNotification()
        private set


    fun updateNotification(state: TeacherServiceState) {
        notification = state.toNotification()
        notificationManager.notifyIfPermissionGranted(
            applicationContext,
            notificationID,
            notification
        )
    }

    private fun TeacherServiceState.toNotification(): Notification = when (this) {
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
