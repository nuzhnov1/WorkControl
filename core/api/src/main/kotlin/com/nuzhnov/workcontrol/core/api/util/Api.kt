package com.nuzhnov.workcontrol.core.api.util


suspend inline fun <T : Any> safeApiCall(
    crossinline apiCall: suspend () -> T
): ResponseResult<T> = runCatching { apiCall() }.toResponseResult()
