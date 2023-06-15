package com.nuzhnov.workcontrol.core.models.util

sealed interface LoadResult {
    object Success : LoadResult

    sealed interface Failure : LoadResult {
        data class NotFounded(val cause: Throwable) : Failure
        data class TooManyRequests(val cause: Throwable) : Failure
        data class ServiceError(val cause: Throwable) : Failure
        data class NetworkError(val cause: Throwable) : Failure
        data class UnknownError(val cause: Throwable) : Failure
    }
}
