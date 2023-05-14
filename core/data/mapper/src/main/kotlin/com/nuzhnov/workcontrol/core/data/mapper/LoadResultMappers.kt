package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.model.util.LoadResult


fun <T> Response<T>.toLoadResult(): LoadResult<T> = when (this) {
    is Response.Success -> LoadResult.Success(data = value)
    is Response.Failure.HttpClientError -> this.toLoadResult()
    is Response.Failure.HttpServerError -> LoadResult.Failure.ServiceError(cause)
    is Response.Failure.NetworkError -> LoadResult.Failure.NetworkError(cause)
    is Response.Failure.UnknownError -> LoadResult.Failure.UnknownError(cause)
}

@PublishedApi
internal fun <T> Response.Failure.HttpClientError.toLoadResult(): LoadResult<T> = when (this) {
    is Response.Failure.HttpClientError.NotFound -> LoadResult.Failure.NotFounded(cause)
    is Response.Failure.HttpClientError.TooManyRequests -> LoadResult.Failure.TooManyRequests(cause)
    else -> LoadResult.Failure.UnknownError(cause)
}

inline fun <T, R> Response<T>.toLoadResult(transform: (T) -> R): LoadResult<R> = when (this) {
    is Response.Success -> LoadResult.Success(data = transform(value))
    is Response.Failure.HttpClientError -> this.toLoadResult()
    is Response.Failure.HttpServerError -> LoadResult.Failure.ServiceError(cause)
    is Response.Failure.NetworkError -> LoadResult.Failure.NetworkError(cause)
    is Response.Failure.UnknownError -> LoadResult.Failure.UnknownError(cause)
}

fun <T> Result<LoadResult<T>>.unwrap(): LoadResult<T> = this.fold(
    onSuccess = { it },
    onFailure = { cause -> LoadResult.Failure.UnknownError(cause) }
)
