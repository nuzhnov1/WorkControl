package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.remote.api.ControlServerApi
import com.nuzhnov.workcontrol.shared.visitservice.data.remote.mapper.toModelSet
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class VisitorsRemoteDataSource @Inject constructor(
    api: ControlServerApi,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher
) {
    val visitors = api.visitors
        .map { networkModelSet -> networkModelSet.toModelSet() }
        .flowOn(coroutineDispatcher)
}
