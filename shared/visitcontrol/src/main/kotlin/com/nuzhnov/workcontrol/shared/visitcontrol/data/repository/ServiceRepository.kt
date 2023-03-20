package com.nuzhnov.workcontrol.shared.visitcontrol.data.repository

import com.nuzhnov.controlservice.data.api.model.ClientApiModel
import com.nuzhnov.workcontrol.shared.visitcontrol.data.model.Role
import com.nuzhnov.workcontrol.shared.visitcontrol.data.model.ServiceState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

abstract class ServiceRepository {

    protected abstract val coroutineScope: CoroutineScope

    private val _role = MutableStateFlow<Role?>(value = null)
    val role = _role.asStateFlow()

    private val _serviceState = MutableStateFlow<ServiceState?>(value = null)
    val serviceState = _serviceState.asStateFlow()

    private val _clients = MutableStateFlow(value = mutableMapOf<Long, ClientApiModel>())
    val clients: StateFlow<Set<ClientApiModel>> = _clients.map { it }


    fun setRole(role: Role) {
        _role.value = role
    }

    fun updateServiceState(serviceState: ServiceState) {
        _serviceState.value = serviceState
    }

    suspend fun startServer() = withContext(Dispatchers.IO) {
        val localHost = InetAddress.getLocalHost() // Possible UnknownHostException
        val socketAddress = InetSocketAddress(localHost, SERVER_PORT)
        val selector = Selector.open() // Possible unknown IOException
        val serverSocketChannel = ServerSocketChannel.open().apply { // Possible unknown IOException
            configureBlocking(/* block = */ false) // Possible unknown IOException
            socket().bind(socketAddress, BACKLOG) // Possible IOException related with busy port
            register(selector, SelectionKey.OP_ACCEPT)
        }
        val longBuffer = ByteBuffer.allocate(/* capacity = */ Long.SIZE_BYTES)
        val mapSocketAddressToID = mutableMapOf<SocketAddress, Long?>()

        while (true) {
            // Possible IOException:
            if (selector.selectNow() <= 0) {
                yield() // CancellationException
                continue
            }

            val selectedKeys = selector.selectedKeys()
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = iterator.next()
                iterator.remove()

                when {
                    // Possible exceptions: SecurityException or IOException
                    key.isAcceptable -> serverSocketChannel.accept().apply {
                        val clientSocket = socket()
                        val clientSocketAddress = clientSocket.remoteSocketAddress

                        mapSocketAddressToID[clientSocketAddress] = null
                        configureBlocking(/* block = */ false) // Possible unknown IOException
                        register(selector, SelectionKey.OP_READ)
                    }

                    key.isReadable -> {
                        val socketChannel = key.channel() as? SocketChannel ?: continue
                        val clientSocketAddress = socketChannel.socket().remoteSocketAddress
                        val clientID = mapSocketAddressToID[clientSocketAddress]

                        val readBytesCount = socketChannel.read(longBuffer)

                        when {
                            // Connection with client has been lost or error
                            readBytesCount < 0 -> {
                                if (clientID != null) {
                                    updateClient(clientID, isActiveNow = false)
                                }

                                socketChannel.close()
                            }

                            // Error was occurred
                            readBytesCount != Long.SIZE_BYTES -> {
                                if (clientID != null) {
                                    updateClient(clientID, isActiveNow = false)
                                }
                            }

                            else -> {
                                val id = longBuffer.long

                                val uniqueID = if (clientID == null) {
                                    mapSocketAddressToID[clientSocketAddress] = id
                                    id
                                } else {
                                    clientID
                                }

                                updateClient(uniqueID, isActiveNow = true)
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun startClient(serverAddress: InetAddress) = withContext(Dispatchers.IO) {
        val socketAddress = InetSocketAddress(serverAddress, SERVER_PORT)
        val selector = Selector.open()
        val clientSocketChannel = SocketChannel.open().apply {
            configureBlocking(/* block = */ false)
            register(selector, SelectionKey.OP_CONNECT or SelectionKey.OP_WRITE)
            connect(socketAddress)
        }
        val longBuffer = ByteBuffer.allocate(/* capacity = */ Long.SIZE_BYTES)

        while (true) {
            if (selector.selectNow() <= 0) {
                yield()
                continue
            }

            val selectedKeys = selector.selectedKeys()
            val iterator = selectedKeys.iterator()

            while (iterator.hasNext()) {
                val key = iterator.next()
                iterator.remove()

                when {
                    key.isConnectable -> {
                        try {
                            if (clientSocketChannel.isConnectionPending) {
                                clientSocketChannel.finishConnect()
                            }
                        } catch (e: IOException) {
                            key.cancel()
                        }
                    }


                }
            }
        }
    }


    companion object {
        const val SERVER_PORT = 48_793
        const val BACKLOG = 1024
    }
}
