package com.nuzhnov.workcontrol.shared.visitservice.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


fun <T> Flow<T>.throttleLatest(delayMillis: Long) = this
    .conflate()
    .transform {
        emit(it)
        delay(delayMillis)
    }
