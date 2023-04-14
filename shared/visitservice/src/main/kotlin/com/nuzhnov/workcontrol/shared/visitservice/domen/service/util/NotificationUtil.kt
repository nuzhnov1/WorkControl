package com.nuzhnov.workcontrol.shared.visitservice.domen.service.util

import com.nuzhnov.workcontrol.shared.visitservice.R
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


internal const val NOTIFICATION_GROUP_ID = "com.nuzhnov.workcontrol.shared.visitservice." +
        "domen.service.notificationGroupId"

internal const val NOTIFICATION_CHANNEL_GROUP_ID = "com.nuzhnov.workcontrol.shared.visitservice." +
        "domen.service.notificationChannelGroupId"

internal const val NOTIFICATION_ON_TIME_IN_MILLIS = 1_000
internal const val NOTIFICATION_OFF_TIME_IN_MILLIS = 1_000

internal val defaultNotificationChannelVibrationPattern = longArrayOf(0, 400, 300, 400, 0)
internal val defaultNotificationSound: Uri = Settings.System.DEFAULT_NOTIFICATION_URI

internal val defaultNotificationAudioAttributes
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) get() = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

internal val Context.notificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

internal val Context.defaultNotificationAccentColor get() = ContextCompat.getColor(
    /* context = */ this,
    /* id = */ R.color.default_notification_accent_color
)

internal val Context.defaultNotificationLightColor get() = ContextCompat.getColor(
    /* context = */ this,
    /* id = */ R.color.default_notification_light_color
)


internal fun Context.createNotificationChannelGroup() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val groupId = NOTIFICATION_CHANNEL_GROUP_ID
        val groupName = getString(R.string.notification_group_name)
        val group = NotificationChannelGroup(groupId, groupName)

        notificationManager?.createNotificationChannelGroup(group)
    }
}
