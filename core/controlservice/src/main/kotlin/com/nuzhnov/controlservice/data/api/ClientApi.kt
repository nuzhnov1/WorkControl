package com.nuzhnov.controlservice.data.api

import com.nuzhnov.controlservice.data.model.Client

interface ClientApi {
    suspend fun sendMacAddressAndId(macAddress: String, id: Long)
    suspend fun receiveClientAndMacAddress(): Pair<String, Client>
}
