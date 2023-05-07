package com.nuzhnov.workcontrol.core.api.util

sealed interface Response<out T> {
    data class Success<T>(val value: T) : Response<T>

    sealed interface Failure : Response<Nothing> {
        sealed interface HttpClientError : Failure {
            data class BadRequest(val message: String) : HttpClientError
            data class Unauthorized(val message: String) : HttpClientError
            data class Forbidden(val message: String) : HttpClientError
            data class NotFound(val message: String) : HttpClientError
            data class TooManyRequests(val message: String) : HttpClientError
            data class UnknownError(val message: String) : HttpClientError
        }

        sealed interface HttpServerError : Failure {
            data class InternalServerError(val message: String) : HttpServerError
            data class BadGateway(val message: String) : HttpServerError
            data class ServiceUnavailable(val message: String) : HttpServerError
            data class GatewayTimeout(val message: String) : HttpServerError
            data class UnknownError(val message: String) : HttpServerError
        }

        sealed interface NetworkError : Failure {
            data class UnknownHost(val cause: Throwable) : NetworkError
            data class ConnectError(val cause: Throwable) : NetworkError
            data class UnknownError(val cause: Throwable) : NetworkError
        }

        data class UnknownError(val cause: Throwable) : Failure
    }
}
