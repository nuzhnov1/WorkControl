package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlServerApi
import javax.inject.Inject

internal class ServerStateLocalDataSource @Inject constructor(api: VisitControlServerApi) {
    val serverState = api.serverState
}
