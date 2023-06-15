package com.nuzhnov.workcontrol.core.models.util

sealed interface OperationResult {
    object Success : OperationResult
    object Failure : OperationResult
}
