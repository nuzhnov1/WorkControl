package com.nuzhnov.workcontrol.core.api.util

import retrofit2.HttpException

sealed interface Response<out T> {
    data class Success<T>(val value: T) : Response<T>

    sealed class Failure(open val cause: Throwable) : Response<Nothing> {
        sealed class HttpClientError(override val cause: HttpException) : Failure(cause) {
            class BadRequest(cause: HttpException) : HttpClientError(cause)
            class Unauthorized(cause: HttpException) : HttpClientError(cause)
            class Forbidden(cause: HttpException) : HttpClientError(cause)
            class NotFound(cause: HttpException) : HttpClientError(cause)
            class TooManyRequests(cause: HttpException) : HttpClientError(cause)
            class UnknownError(cause: HttpException) : HttpClientError(cause)
        }

        sealed class HttpServerError(override val cause: HttpException) : Failure(cause) {
            class InternalServerError(cause: HttpException) : HttpServerError(cause)
            class BadGateway(cause: HttpException) : HttpServerError(cause)

            class ServiceUnavailable(cause: HttpException) : HttpServerError(cause)

            class GatewayTimeout(cause: HttpException) : HttpServerError(cause)

            class UnknownError(cause: HttpException) : HttpServerError(cause)
        }

        sealed class NetworkError(cause: Throwable) : Failure(cause) {
            class UnknownHost(cause: Throwable) : NetworkError(cause)
            class ConnectError(cause: Throwable) : NetworkError(cause)
            class UnknownError(cause: Throwable) : NetworkError(cause)
        }

        class UnknownError(cause: Throwable) : Failure(cause)
    }
}
