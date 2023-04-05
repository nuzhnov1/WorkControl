package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.api.VisitorApi
import javax.inject.Inject

internal class VisitorStateLocalDataSource @Inject constructor(api: VisitorApi) {
    val visitorState = api.visitorState
}
