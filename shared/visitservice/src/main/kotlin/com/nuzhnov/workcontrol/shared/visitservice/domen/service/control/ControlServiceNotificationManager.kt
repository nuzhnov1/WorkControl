package com.nuzhnov.workcontrol.shared.visitservice.domen.service.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceInitFailedReason
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceInitFailedReason.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceError
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceError.*
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.util.*
import com.nuzhnov.workcontrol.shared.visitservice.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

internal class ControlServiceNotificationManager(
    private val context: Context,
    private val notificationID: Int,
    private val activityClass: Class<*>
) {

    private val contentPendingIntent = run {
        val contentIntent = Intent(context, activityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(/* nextIntent = */ contentIntent)
            getPendingIntent(/* requestCode = */ 0, /* flags = */ pendingIntentFlags)
        }
    }

    @Suppress("DEPRECATION")
    private val notificationBuilder = NotificationCompat.Builder(
        /* context = */ context,
        /* channelId = */ NOTIFICATION_CHANNEL_ID
    ).apply {
        setSmallIcon(/* icon = */ R.drawable.control_service_24)
        // setLargeIcon()
        setContentTitle(/* title = */ context.getString(R.string.control_service_title))
        setContentIntent(/* intent = */ contentPendingIntent)
        setColorized(/* colorize = */ true)
        color = context.defaultNotificationAccentColor

        setSound(/* sound = */ defaultNotificationSound)
        setVibrate(/* pattern = */ defaultNotificationChannelVibrationPattern)

        setLights(
            /* argb = */ context.defaultNotificationLightColor,
            /* onMs = */ NOTIFICATION_ON_TIME_IN_MILLIS,
            /* offMs = */ NOTIFICATION_OFF_TIME_IN_MILLIS
        )

        priority = Notification.PRIORITY_MAX
        setVisibility(NotificationCompat.VISIBILITY_PRIVATE)

        setGroup(/* groupKey = */ NOTIFICATION_GROUP_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setCategory(Notification.CATEGORY_STATUS)
        }

        setOngoing(/* ongoing = */ true)
        setAllowSystemGeneratedContextualActions(/* allowed = */ false)
        setAutoCancel(/* autoCancel */ false)
        setLocalOnly(false)
    }

    private var previousNotification: Notification? = null


    internal fun createControlServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = NOTIFICATION_CHANNEL_ID
            val name = context.getString(R.string.control_service_channel_name)
            val importanceLevel = NotificationManager.IMPORTANCE_HIGH

            val notificationChannel = NotificationChannel(id, name, importanceLevel).apply {
                group = NOTIFICATION_CHANNEL_GROUP_ID
                description = context.getString(
                    R.string.control_service_notification_channel_description
                )
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE

                setSound(defaultNotificationSound, defaultNotificationAudioAttributes)

                enableVibration(/* vibration = */ true)
                shouldVibrate()
                setBypassDnd(/* bypassDnd = */ true)
                vibrationPattern = defaultNotificationChannelVibrationPattern

                enableLights(/* lights = */ true)
                shouldShowLights()
                lightColor = context.defaultNotificationLightColor
            }

            context.notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    internal fun ControlServiceState.toNotification(): Notification? {
        val currentTimeMillis = System.currentTimeMillis()
        val newNotification = when (this) {
            is NotInitialized -> notificationBuilder.setContentText(
                context.getString(R.string.control_service_not_initialized_status)
            )

            is InitFailed -> notificationBuilder.setContentText(
                context.getString(
                    R.string.control_service_init_failed_status,
                    reason.toResourceString()
                )
            )

            is Running -> notificationBuilder
                .setContentText(context.getString(R.string.control_service_running_status))
                .setUsesChronometer(true)
                .setWhen(currentTimeMillis)

            is StoppedAcceptConnections -> notificationBuilder
                .setContentText(
                    context.getString(R.string.control_service_stopped_accept_connections_status)
                )
                .setUsesChronometer(true)
                .setWhen(previousNotification?.`when` ?: currentTimeMillis)

            is Stopped -> notificationBuilder
                .setContentText(context.getString(R.string.control_service_stopped_status))
                .setWhen(currentTimeMillis)

            is StoppedByError -> notificationBuilder
                .setContentText(
                    context.getString(
                        R.string.control_service_stopped_by_error_status,
                        error.toResourceString()
                    )
                )
                .setWhen(currentTimeMillis)

            else -> null
        }?.build()

        previousNotification = newNotification
        return newNotification
    }

    internal fun updateNotification(newNotification: Notification) {
        context.notificationManager?.notify(notificationID, newNotification)
    }

    private fun ControlServiceInitFailedReason.toResourceString() = when (this) {
        TECHNOLOGY_UNAVAILABLE_ERROR -> context.getString(
            R.string.control_service_technology_unavailable_init_error
        )
    }

    private fun ControlServiceError.toResourceString() = when (this) {
        REGISTER_SERVICE_FAILED -> context.getString(
            R.string.control_service_register_failed_error
        )

        INIT_ERROR -> context.getString(R.string.control_service_init_failed_error)

        MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED -> context.getString(
            R.string.control_service_max_accept_connections_attempts_reached_error
        )

        IO_ERROR -> context.getString(R.string.control_service_IO_error)

        SECURITY_ERROR -> context.getString(R.string.control_service_security_error)

        UNKNOWN_ERROR -> context.getString(R.string.control_service_unknown_error)
    }


    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "com.nuzhnov.workcontrol.shared." +
                "visitservice.domen.service.control.notificationChannelId"
    }
}
