package com.nuzhnov.controlservice.wifidirect

import android.content.Intent
import android.os.Build
import android.os.Parcelable

internal inline fun <reified T : Parcelable> Intent.getParcelable(key: String): T? = when {
    Build.VERSION.SDK_INT < 33 -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    else -> getParcelableExtra(key, T::class.java)
}
