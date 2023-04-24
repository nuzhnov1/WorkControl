package com.nuzhnov.workcontrol.shared.util

import java.io.Serializable
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build


internal inline fun <reified T : Serializable> Intent.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT < 33 -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    else -> getSerializableExtra(key, T::class.java)
}

internal fun Context.checkPermissionGranted(permission: String): Boolean {
    return checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}
