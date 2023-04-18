package com.nuzhnov.workcontrol.common.visitcontrol.util


internal inline fun <T> T.applyCatching(block: T.() -> Unit): Result<Unit> = try {
    Result.success(block())
} catch (t: Throwable) {
    Result.failure(t)
}
