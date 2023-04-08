package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.ControlServerApi
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.mapper.toModelSet
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class VisitorsRemoteDataSource @Inject constructor(api: ControlServerApi) {
    val visitors = api.visitors.map { networkModelSet -> networkModelSet.toModelSet() }
}
