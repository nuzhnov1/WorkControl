package com.nuzhnov.workcontrol.common.visitcontrol.util

import com.nuzhnov.workcontrol.common.util.applyCatching
import java.io.Closeable
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel


internal val Selector.isEventsNotOccurred get() = selectNow() == 0
internal val Selector.selectedKeys get() = requireNotNull(selectedKeys())
internal val SelectionKey.clientChannel get() = channel() as? SocketChannel

internal fun <T : Closeable> T.safeClose() = applyCatching { close() }
