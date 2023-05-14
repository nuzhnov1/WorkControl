package com.nuzhnov.workcontrol.core.data.api.util

import com.nuzhnov.workcontrol.core.data.api.constant.AUTHENTICATION_FORMAT
import android.util.Base64

internal object Base64Encoder {
    fun encodeAuthData(login: String, password: String): String = Base64.encodeToString(
        /* input = */ String.format(AUTHENTICATION_FORMAT, login, password).toByteArray(),
        /* flags = */ Base64.NO_WRAP
    )
}
