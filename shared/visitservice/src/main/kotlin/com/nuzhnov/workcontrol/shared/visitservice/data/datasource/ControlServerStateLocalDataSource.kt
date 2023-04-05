package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.api.ControlServerApi
import javax.inject.Inject

internal class ControlServerStateLocalDataSource @Inject constructor(api: ControlServerApi) {
    val controlServerState = api.controlServerState
}
