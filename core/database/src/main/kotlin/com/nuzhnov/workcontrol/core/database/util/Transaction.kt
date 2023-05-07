package com.nuzhnov.workcontrol.core.database.util


suspend inline fun <T> safeTransactionExecute(
    crossinline transaction: suspend () -> T
): Result<T> = runCatching { transaction() }
