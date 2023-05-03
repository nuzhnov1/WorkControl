package com.nuzhnov.workcontrol.core.api.util

sealed interface ResponseResult<out T> {
    data class Success<T>(val value: T) : ResponseResult<T>

    sealed interface Failure : ResponseResult<Nothing> {
        sealed interface HttpClientError : Failure {
            object BadRequest       : HttpClientError
            object Unauthorized     : HttpClientError
            object Forbidden        : HttpClientError
            object NotFound         : HttpClientError
            object TooManyRequests  : HttpClientError
            object UnknownError     : HttpClientError
        }

        sealed interface HttpServerError : Failure {
            object InternalServerError  : HttpServerError
            object BadGateway           : HttpServerError
            object ServiceUnavailable   : HttpServerError
            object GatewayTimeout       : HttpServerError
            object UnknownError         : HttpServerError
        }

        sealed interface NetworkError : Failure {
            object UnknownHost  : NetworkError
            object ConnectError : NetworkError
            object UnknownError : NetworkError
        }

        object UnknownError : Failure
    }
}
