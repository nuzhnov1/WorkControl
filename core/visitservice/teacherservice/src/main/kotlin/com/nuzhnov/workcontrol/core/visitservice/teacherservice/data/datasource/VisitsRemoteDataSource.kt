package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.datasource

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.api.ControlServerApi
import javax.inject.Inject

internal class VisitsRemoteDataSource @Inject constructor(api: ControlServerApi) {
    val visitsFlow = api.visitsFlow
}
