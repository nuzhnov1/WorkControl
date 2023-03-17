package com.nuzhnov.controlservice.data.api

import com.nuzhnov.controlservice.data.model.Client

class ClientApiImpl : ClientApi {
    override suspend fun sendMacAddressAndId(macAddress: String, id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun receiveClientAndMacAddress(): Pair<String, Client> {
        TODO("Not yet implemented")
    }
}
