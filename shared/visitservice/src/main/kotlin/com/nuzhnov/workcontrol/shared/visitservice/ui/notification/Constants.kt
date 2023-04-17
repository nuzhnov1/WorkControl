package com.nuzhnov.workcontrol.shared.visitservice.ui.notification

import android.net.Uri
import android.provider.Settings
import androidx.core.app.NotificationCompat


internal const val NOTIFICATION_BADGE_ICON_TYPE = NotificationCompat.BADGE_ICON_SMALL
internal const val NOTIFICATION_PRIORITY = NotificationCompat.PRIORITY_MAX
internal const val NOTIFICATION_VISIBILITY = NotificationCompat.VISIBILITY_PRIVATE
internal const val NOTIFICATION_CATEGORY = NotificationCompat.CATEGORY_STATUS

internal const val NOTIFICATION_ON_TIME_MS = 1_000
internal const val NOTIFICATION_OFF_TIME_MS = 1_000

internal val notificationVibrationPattern = longArrayOf(0, 400, 300, 400, 0)
internal val notificationSound: Uri = Settings.System.DEFAULT_NOTIFICATION_URI


// TODO: move this to the another module
//@get:RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//internal val notificationAudioAttributes
//    get() = AudioAttributes.Builder()
//        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//        .build()
//
//internal const val NOTIFICATION_GROUP_ID = "com.nuzhnov.workcontrol.shared.visitservice." +
//        "domen.service.notificationGroupId"
//
//internal const val NOTIFICATION_CHANNEL_GROUP_ID = "com.nuzhnov.workcontrol.shared.visitservice." +
//        "domen.service.notificationChannelGroupId"
