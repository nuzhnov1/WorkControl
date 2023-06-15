package com.nuzhnov.workcontrol.common.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


inline fun <T> T.applyCatching(block: T.() -> Unit): Result<Unit> = try {
    Result.success(block())
} catch (t: Throwable) {
    Result.failure(t)
}

inline fun <T> Result<T>.transformFailedCause(
    block: (Throwable) -> Throwable
): Result<T> = recoverCatching { cause -> throw block(cause) }

inline fun <K, V> MutableStateFlow<Map<K, V>>.applyUpdate(
    block: MutableMap<K, V>.() -> Unit
) = update { value -> value.toMutableMap().apply(block) }

fun <T> Flow<T>.throttleLatest(delayMillis: Long): Flow<T> = this
    .conflate()
    .transform {
        emit(it)
        delay(delayMillis)
    }
