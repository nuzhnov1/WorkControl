package com.nuzhnov.workcontrol.core.visitservice.notification.resources

import android.content.Context
import androidx.core.content.ContextCompat
import com.nuzhnov.workcontrol.core.visitservice.notification.R


internal val Context.notificationAccentColor
    get() = ContextCompat.getColor(/* context = */ this, R.color.notification_accent_color)

internal val Context.notificationLightColor
    get() = ContextCompat.getColor(/* context = */ this, R.color.notification_light_color)