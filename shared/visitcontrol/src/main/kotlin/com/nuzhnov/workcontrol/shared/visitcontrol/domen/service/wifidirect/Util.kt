package com.nuzhnov.workcontrol.shared.visitcontrol.domen.service.wifidirect

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

internal inline fun <reified T : Parcelable> Intent.getParcelable(key: String): T? = when {
    Build.VERSION.SDK_INT < 33 -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    else -> getParcelableExtra(key, T::class.java)
}

internal inline fun <reified T : Serializable> Intent.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT < 33 -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    else -> getSerializableExtra(key, T::class.java)
}
