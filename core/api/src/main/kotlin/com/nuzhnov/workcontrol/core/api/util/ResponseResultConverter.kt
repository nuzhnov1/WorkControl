package com.nuzhnov.workcontrol.core.api.util

import com.nuzhnov.workcontrol.core.api.util.ResponseResult.Failure
import com.nuzhnov.workcontrol.core.api.util.ResponseResult.Failure.*
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException


@PublishedApi
internal fun <T> Result<T>.toResponseResult(): ResponseResult<T> = this.fold(
    onSuccess = { value -> ResponseResult.Success(value) },
    onFailure = { exception -> exception.toFailure() }
)

private fun Throwable.toFailure(): Failure = when (this) {
    is HttpException    -> this.toHttpFailure()
    is IOException      -> this.toNetworkError()
    else                -> UnknownError
}

private fun HttpException.toHttpFailure(): Failure = when (val code = code()) {
    in 400..499 -> code.toHttpClientFailure()
    in 500..599 -> code.toHttpServerFailure()
    else -> UnknownError
}

private fun Int.toHttpClientFailure(): HttpClientError = when (this) {
    400         -> HttpClientError.BadRequest
    401, 407    -> HttpClientError.Unauthorized
    403         -> HttpClientError.Forbidden
    404         -> HttpClientError.NotFound
    429         -> HttpClientError.TooManyRequests
    else        -> HttpClientError.UnknownError
}

private fun Int.toHttpServerFailure(): HttpServerError = when (this) {
    500     -> HttpServerError.InternalServerError
    502     -> HttpServerError.BadGateway
    503     -> HttpServerError.ServiceUnavailable
    504     -> HttpServerError.GatewayTimeout
    else    -> HttpServerError.UnknownError
}

private fun IOException.toNetworkError(): NetworkError = when (this) {
    is UnknownHostException -> NetworkError.UnknownHost
    is ConnectException     -> NetworkError.ConnectError
    else                    -> NetworkError.UnknownError
}
