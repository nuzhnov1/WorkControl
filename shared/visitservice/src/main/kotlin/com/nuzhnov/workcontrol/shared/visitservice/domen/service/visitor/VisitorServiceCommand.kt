package com.nuzhnov.workcontrol.shared.visitservice.domen.service.visitor

import java.io.Serializable

internal sealed interface VisitorServiceCommand : Serializable {
    object Discover : VisitorServiceCommand
    data class Connect(val serviceName: String) : VisitorServiceCommand
}
