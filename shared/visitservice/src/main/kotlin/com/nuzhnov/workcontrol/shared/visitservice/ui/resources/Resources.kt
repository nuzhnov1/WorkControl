package com.nuzhnov.workcontrol.shared.visitservice.ui.resources

import com.nuzhnov.workcontrol.shared.visitservice.R
import android.content.Context
import androidx.core.content.ContextCompat


internal val Context.notificationAccentColor
    get() = ContextCompat.getColor(/* context = */ this, R.color.notification_accent_color)

internal val Context.notificationLightColor
    get() = ContextCompat.getColor(/* context = */ this, R.color.notification_light_color)

internal val Context.controlServiceTitle
    get() = getString(R.string.control_service_title)

internal val Context.visitorServiceTitle
    get() = getString(R.string.visitor_service_title)
