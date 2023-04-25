package com.nuzhnov.workcontrol.core.visitservice.util.extensions

import java.io.Serializable
import android.content.Intent
import android.os.Build


inline fun <reified T : Serializable> Intent.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT < 33 -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    else -> getSerializableExtra(key, T::class.java)
}
