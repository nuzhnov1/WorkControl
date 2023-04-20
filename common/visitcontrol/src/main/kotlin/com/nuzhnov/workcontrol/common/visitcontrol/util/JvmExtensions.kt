package com.nuzhnov.workcontrol.common.visitcontrol.util

import java.io.Closeable
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


internal val Selector.isEventsNotOccurred get() = selectNow() == 0
internal val Selector.selectedKeys get() = selectedKeys()
internal val SelectionKey.serverChannel get() = channel() as? ServerSocketChannel
internal val SelectionKey.clientChannel get() = channel() as? SocketChannel

internal fun <T : Closeable> T.safeClose() = applyCatching { close() }
