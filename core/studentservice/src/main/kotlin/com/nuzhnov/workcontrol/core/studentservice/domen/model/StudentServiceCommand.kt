package com.nuzhnov.workcontrol.core.studentservice.domen.model

import java.io.Serializable

internal sealed interface StudentServiceCommand : Serializable {
    object Discover : StudentServiceCommand
    data class Connect(val serviceName: String) : StudentServiceCommand
}
