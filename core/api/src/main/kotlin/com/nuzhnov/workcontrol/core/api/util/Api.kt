package com.nuzhnov.workcontrol.core.api.util


suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): Response<T> = runCatching { apiCall() }.toResponseResult()
