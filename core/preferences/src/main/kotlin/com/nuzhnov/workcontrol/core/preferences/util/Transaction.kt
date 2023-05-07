package com.nuzhnov.workcontrol.core.preferences.util


suspend inline fun <T> safeTransactionExecute(
    crossinline transaction: suspend () -> T
): Result<T> = runCatching { transaction() }
