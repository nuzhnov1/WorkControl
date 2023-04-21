package com.nuzhnov.workcontrol.common.visitcontrol.visitor

import com.nuzhnov.workcontrol.common.visitcontrol.annotation.Unique
import com.nuzhnov.workcontrol.common.visitcontrol.model.VisitorID
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

interface Visitor {
    val state: StateFlow<VisitorState>


    suspend fun startVisit(
        serverAddress: InetAddress,
        serverPort: Int,
        @Unique visitorID: VisitorID
    )

    fun stopVisit()


    companion object {
        fun getDefaultVisitor(): Visitor = VisitorImpl()
    }
}
