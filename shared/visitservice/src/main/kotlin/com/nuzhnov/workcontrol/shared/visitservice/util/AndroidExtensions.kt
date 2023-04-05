package com.nuzhnov.workcontrol.shared.visitservice.util

import android.content.Intent
import android.os.Build
import java.io.Serializable


internal inline fun <reified T : Serializable> Intent.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT < 33 -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    else -> getSerializableExtra(key, T::class.java)
}