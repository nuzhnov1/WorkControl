package com.nuzhnov.workcontrol.core.api.util


suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): Result<T> = runCatching { apiCall() }
