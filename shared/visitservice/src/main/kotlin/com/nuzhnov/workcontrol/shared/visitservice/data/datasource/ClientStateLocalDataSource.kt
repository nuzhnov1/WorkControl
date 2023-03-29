package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlClientApi
import javax.inject.Inject

internal class ClientStateLocalDataSource @Inject constructor(api: VisitControlClientApi) {
    val clientState = api.clientState
}
