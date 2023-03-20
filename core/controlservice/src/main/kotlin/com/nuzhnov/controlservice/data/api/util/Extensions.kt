package com.nuzhnov.controlservice.data.api.util

import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


internal val Selector.isEventsNotOccurred get() = selectNow() == 0
internal val Selector.selectedKeys get() = selectedKeys()
internal val SelectionKey.server get() = channel() as? ServerSocketChannel
internal val SelectionKey.client get() = channel() as? SocketChannel
