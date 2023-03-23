package com.nuzhnov.workcontrol.core.controlservice.common

import java.io.Closeable
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


internal val Selector.isEventsNotOccurred get() = selectNow() == 0
internal val Selector.selectedKeys get() = selectedKeys()
internal val SelectionKey.server get() = channel() as? ServerSocketChannel
internal val SelectionKey.client get() = channel() as? SocketChannel


// TODO: possible to put it in a separate module
internal inline fun <T> T.applyCatching(block: T.() -> Unit): Result<Unit> = try {
    Result.success(block())
} catch (t: Throwable) {
    Result.failure(t)
}

internal fun <T : Closeable> T.safeClose() = applyCatching { close() }
