package com.nuzhnov.workcontrol.core.api.util

import com.nuzhnov.workcontrol.core.api.util.Response.Failure
import com.nuzhnov.workcontrol.core.api.util.Response.Failure.*
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import retrofit2.HttpException


@PublishedApi
internal fun <T> Result<T>.toResponseResult(): Response<T> = this.fold(
    onSuccess = { value -> Response.Success(value) },
    onFailure = { exception -> exception.toFailure() }
)

private fun Throwable.toFailure(): Failure = when (this) {
    is HttpException -> this.toHttpFailure()
    is IOException -> this.toNetworkError()
    else -> UnknownError(cause = this)
}

private fun HttpException.toHttpFailure(): Failure = when (code()) {
    in 400..499 -> this.toHttpClientFailure()
    in 500..599 -> this.toHttpServerFailure()
    else -> UnknownError(cause = this)
}

private fun HttpException.toHttpClientFailure(): HttpClientError = when (code()) {
    400 -> HttpClientError.BadRequest(cause = this)
    401, 407 -> HttpClientError.Unauthorized(cause = this)
    403 -> HttpClientError.Forbidden(cause = this)
    404 -> HttpClientError.NotFound(cause = this)
    429 -> HttpClientError.TooManyRequests(cause = this)
    else -> HttpClientError.UnknownError(cause = this)
}

private fun HttpException.toHttpServerFailure(): HttpServerError = when (code()) {
    500 -> HttpServerError.InternalServerError(cause = this)
    502 -> HttpServerError.BadGateway(cause = this)
    503 -> HttpServerError.ServiceUnavailable(cause = this)
    504 -> HttpServerError.GatewayTimeout(cause = this)
    else -> HttpServerError.UnknownError(cause = this)
}

private fun IOException.toNetworkError(): NetworkError = when (this) {
    is UnknownHostException -> NetworkError.UnknownHost(cause = this)
    is ConnectException -> NetworkError.ConnectError(cause = this)
    else -> NetworkError.UnknownError(cause = this)
}
