package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model

import java.net.InetAddress

data class DiscoveredService(
    val name: String,
    val host: InetAddress,
    val port: Int
)
