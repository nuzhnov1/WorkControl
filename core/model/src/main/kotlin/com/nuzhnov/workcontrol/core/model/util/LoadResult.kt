package com.nuzhnov.workcontrol.core.model.util

sealed interface LoadResult<out T> {
    data class Success<T>(val data: T) : LoadResult<T>

    sealed interface Failure : LoadResult<Nothing> {
        object NotFounded : Failure
        object TooManyRequests : Failure
        object ServiceError : Failure
        object NetworkError: Failure
        object UnknownError: Failure
    }
}
