package com.nuzhnov.workcontrol.core.api.authenticator

import com.nuzhnov.workcontrol.core.api.constant.Headers
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

internal class ServiceAuthenticator @Inject constructor(
    private val appPreferences: AppPreferences
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header(Headers.X_ACCESS_TOKEN) != null) {
            return null
        }

        val token = appPreferences.getSessionSync()?.authorizationToken ?: return null

        return response.request.newBuilder()
            .addHeader(Headers.X_ACCESS_TOKEN, token)
            .build()
    }
}
