package com.nuzhnov.workcontrol.core.data.api.util

import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute


suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
) = safeExecute(apiCall).toResponseResult()
