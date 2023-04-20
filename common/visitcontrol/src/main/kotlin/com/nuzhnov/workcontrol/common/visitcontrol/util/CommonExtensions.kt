package com.nuzhnov.workcontrol.common.visitcontrol.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


internal inline fun <T> T.applyCatching(block: T.() -> Unit): Result<Unit> = try {
    Result.success(block())
} catch (t: Throwable) {
    Result.failure(t)
}

internal inline fun <T> Result<T>.transformFailedCause(
    block: (Throwable) -> Throwable
) = recoverCatching { cause -> throw block(cause) }

internal inline fun <K, V> MutableStateFlow<Map<K, V>>.applyUpdate(
    block: MutableMap<K, V>.() -> Unit
) = update { value -> value.toMutableMap().apply(block) }
