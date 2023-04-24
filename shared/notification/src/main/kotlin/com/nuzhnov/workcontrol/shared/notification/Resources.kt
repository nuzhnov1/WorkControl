package com.nuzhnov.workcontrol.shared.notification

import android.content.Context
import androidx.core.content.ContextCompat


internal val Context.notificationAccentColor
    get() = ContextCompat.getColor(/* context = */ this, R.color.notification_accent_color)

internal val Context.notificationLightColor
    get() = ContextCompat.getColor(/* context = */ this, R.color.notification_light_color)