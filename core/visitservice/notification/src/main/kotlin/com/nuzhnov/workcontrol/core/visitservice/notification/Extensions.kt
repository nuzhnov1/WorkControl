package com.nuzhnov.workcontrol.core.visitservice.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun Context.getNotificationBuilderTemplate(
    notificationChannel: String
) = NotificationCompat.Builder(/* context = */ this, notificationChannel).apply {
    setBadgeIconType(NOTIFICATION_BADGE_ICON_TYPE)

    setColorized(true)
    color = notificationAccentColor

    setSound(notificationSound)
    setVibrate(notificationVibrationPattern)

    setLights(
        /* argb = */ notificationLightColor,
        /* onMs = */ NOTIFICATION_ON_TIME_MS,
        /* offMs = */ NOTIFICATION_OFF_TIME_MS
    )

    priority = NOTIFICATION_PRIORITY
    setVisibility(NOTIFICATION_VISIBILITY)
    setCategory(NOTIFICATION_CATEGORY)

    setOngoing(true)
    setAllowSystemGeneratedContextualActions(false)
    setAutoCancel(false)
}

fun Context.getContentPendingIntent(clazz: Class<*>): PendingIntent {
    val contentIntent = Intent(applicationContext, clazz).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    return TaskStackBuilder.create(applicationContext).run {
        addNextIntentWithParentStack(contentIntent)
        getPendingIntent(/* requestCode = */ 0, /* flags = */ pendingIntentFlags)
    }
}

@SuppressLint("MissingPermission")
fun NotificationManagerCompat.notifyIfPermissionGranted(
    context: Context,
    notificationID: Int,
    notification: Notification
) {
    val applicationContext = context.applicationContext

    if (applicationContext.checkPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
        notify(notificationID, notification)
    }
}

internal fun Context.checkPermissionGranted(permission: String): Boolean {
    return checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}

// TODO: move this to the another module
//internal fun Context.createNotificationChannelGroup() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val groupId = NOTIFICATION_CHANNEL_GROUP_ID
//        val groupName = getString(R.string.notification_channel_group_name)
//        val groupDescription = getString(R.string.notification_channel_group_description)
//
//        val group = NotificationChannelGroup(groupId, groupName).apply {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                description = groupDescription
//            }
//        }
//
//        notificationManager?.createNotificationChannelGroup(group)
//    }
//}
//
//private fun createControlServiceNotificationChannel() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val id = NOTIFICATION_CHANNEL_ID
//        val name = applicationContext.getString(R.string.control_service_channel_name)
//        val importanceLevel = NotificationManager.IMPORTANCE_HIGH
//
//        val notificationChannel = NotificationChannel(id, name, importanceLevel).apply {
//            group = NOTIFICATION_CHANNEL_GROUP_ID
//            description = applicationContext.getString(
//                R.string.control_service_notification_channel_description
//            )
//            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//            setSound(notificationSound, notificationAudioAttributes)
//
//            enableVibration(/* vibration = */ true)
//            setBypassDnd(/* bypassDnd = */ true)
//            vibrationPattern = notificationVibrationPattern
//
//            enableLights(/* lights = */ true)
//            lightColor = applicationContext.notificationLightColor
//        }
//
//        applicationContext.notificationManager?.createNotificationChannel(notificationChannel)
//    }
//}
//
//@RequiresApi(api = Build.VERSION_CODES.O)
//internal fun Context.createVisitorServiceNotificationChannel() {
//    val id = VISITOR_SERVICE_NOTIFICATION_CHANNEL_ID
//    val name = getString(R.string.visitor_service_default_channel_name)
//    val importanceLevel = NotificationManager.IMPORTANCE_HIGH
//
//    val notificationChannel = NotificationChannel(id, name, importanceLevel).apply {
//        group = NOTIFICATION_CHANNEL_GROUP_ID
//        description = getString(R.string.visitor_service_default_notification_channel_description)
//        lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//
//        setSound(notificationSound, notificationAudioAttributes)
//
//        enableVibration(/* vibration = */ true)
//        shouldVibrate()
//        setBypassDnd(/* bypassDnd = */ true)
//        vibrationPattern = notificationVibrationPattern
//
//        enableLights(/* lights = */ true)
//        shouldShowLights()
//        lightColor = DEFAULT_NOTIFICATION_CHANNEL_COLOR
//    }
//
//    notificationManager?.createNotificationChannel(notificationChannel)
//}
//