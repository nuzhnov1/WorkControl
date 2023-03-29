package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitControlServerApi
import javax.inject.Inject

internal class VisitorsRemoteDataSource @Inject constructor(api: VisitControlServerApi) {
    val visitors = api.visitors
}
