package com.nuzhnov.workcontrol.core.util.coroutines.util

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


suspend inline fun <T> safeExecute(crossinline block: suspend () -> T) = runCatching { block() }

suspend inline fun <T> safeExecute(
    context: CoroutineContext,
    crossinline block: suspend () -> T
) = withContext(context) { runCatching { block() } }
