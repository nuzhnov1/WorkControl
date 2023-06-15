package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.util.OperationResult


fun Result<Unit>.toOperationResult() = fold(
    onSuccess = { OperationResult.Success },
    onFailure = { OperationResult.Failure }
)
