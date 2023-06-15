package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.models.util.LoadResult


fun <T> Response<T>.toLoadResult(): LoadResult = when (this) {
    is Response.Success -> LoadResult.Success
    is Response.Failure.HttpClientError -> this.toLoadResult()
    is Response.Failure.HttpServerError -> LoadResult.Failure.ServiceError(cause)
    is Response.Failure.NetworkError -> LoadResult.Failure.NetworkError(cause)
    is Response.Failure.UnknownError -> LoadResult.Failure.UnknownError(cause)
}

internal fun Response.Failure.HttpClientError.toLoadResult(): LoadResult = when (this) {
    is Response.Failure.HttpClientError.NotFound -> LoadResult.Failure.NotFounded(cause)
    is Response.Failure.HttpClientError.TooManyRequests -> LoadResult.Failure.TooManyRequests(cause)
    else -> LoadResult.Failure.UnknownError(cause)
}

fun Result<LoadResult>.unwrap(): LoadResult = this.fold(
    onSuccess = { it },
    onFailure = { cause -> LoadResult.Failure.UnknownError(cause) }
)
