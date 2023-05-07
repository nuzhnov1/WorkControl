package com.nuzhnov.workcontrol.core.api.util

import com.nuzhnov.workcontrol.core.api.util.Response.Failure
import com.nuzhnov.workcontrol.core.api.util.Response.Failure.*
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException


@PublishedApi
internal fun <T> Result<T>.toResponseResult(): Response<T> = this.fold(
    onSuccess = { value -> Response.Success(value) },
    onFailure = { exception -> exception.toFailure() }
)

private fun Throwable.toFailure(): Failure = when (this) {
    is HttpException    -> this.toHttpFailure()
    is IOException      -> this.toNetworkError()
    else                -> UnknownError(cause = this)
}

private fun HttpException.toHttpFailure(): Failure = when (code()) {
    in 400..499 -> this.toHttpClientFailure()
    in 500..599 -> this.toHttpServerFailure()
    else -> UnknownError(cause = this)
}

private fun HttpException.toHttpClientFailure(): HttpClientError = when (code()) {
    400         -> HttpClientError.BadRequest(message = this.message())
    401, 407    -> HttpClientError.Unauthorized(message = this.message())
    403         -> HttpClientError.Forbidden(message = this.message())
    404         -> HttpClientError.NotFound(message = this.message())
    429         -> HttpClientError.TooManyRequests(message = this.message())
    else        -> HttpClientError.UnknownError(message = this.message())
}

private fun HttpException.toHttpServerFailure(): HttpServerError = when (code()) {
    500     -> HttpServerError.InternalServerError(message = this.message())
    502     -> HttpServerError.BadGateway(message = this.message())
    503     -> HttpServerError.ServiceUnavailable(message = this.message())
    504     -> HttpServerError.GatewayTimeout(message = this.message())
    else    -> HttpServerError.UnknownError(message = this.message())
}

private fun IOException.toNetworkError(): NetworkError = when (this) {
    is UnknownHostException -> NetworkError.UnknownHost(cause = this)
    is ConnectException     -> NetworkError.ConnectError(cause = this)
    else                    -> NetworkError.UnknownError(cause = this)
}
