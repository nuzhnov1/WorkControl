package com.nuzhnov.workcontrol.core.api.util

import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlin.coroutines.CoroutineContext


suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): Response<T> = safeExecute(apiCall).toResponseResult()

suspend inline fun <T> safeApiCall(
    context: CoroutineContext,
    crossinline apiCall: suspend () -> T
): Response<T> = safeExecute(context, apiCall).toResponseResult()
