package com.nuzhnov.workcontrol.core.model.util

sealed interface LoadResult<out T> {
    data class Success<T>(val data: T) : LoadResult<T>

    sealed interface Failure : LoadResult<Nothing> {
        data class NotFounded(val cause: Throwable) : Failure
        data class TooManyRequests(val cause: Throwable) : Failure
        data class ServiceError(val cause: Throwable) : Failure
        data class NetworkError(val cause: Throwable) : Failure
        data class UnknownError(val cause: Throwable) : Failure
    }
}
